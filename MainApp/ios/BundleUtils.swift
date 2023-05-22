import ZIPFoundation
import CryptoSwift

class BundleUtils {
  static func download(from sourceUrl: String, completion: @escaping (URL) -> Void) {
    guard let uri = URL(string: sourceUrl) else {
      return
    }
    
    
    BundleUtils.downloadZipArchive(from: uri) { url, error in
      if error != nil {
        print(error)
        return
      }
      
      guard let downloadedSourceUri = url else { return }
      
      if !sourceUrl.contains(".zip") {
        DispatchQueue.main.async {
          completion(downloadedSourceUri)
        }
        return
      }
      
      BundleUtils.unarchiveZipArchive(at: downloadedSourceUri) { success, path, error in
        if success {
          DispatchQueue.main.async {
            completion(path!)
          }
        } else {
          print(error)
        }
      }
    }
  }
  
  private static func downloadZipArchive(from url: URL, completion: @escaping (URL?, Error?) -> Void) {
    let task = URLSession.shared.downloadTask(with: url) { (tempLocalURL, response, error) in
      if let tempLocalURL = tempLocalURL {
        let documentsURL = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first!
        let destinationURL = documentsURL.appendingPathComponent(url.absoluteString.md5())
        
        do {
          try? FileManager.default.removeItem(at: destinationURL)
          try FileManager.default.moveItem(at: tempLocalURL, to: destinationURL)
          completion(destinationURL, nil)
        } catch {
          completion(nil, error)
        }
      } else {
        completion(nil, error)
      }
    }
    
    task.resume()
  }
  
  private static func unarchiveZipArchive(at url: URL, completion: @escaping (Bool, URL?, Error?) -> Void) {
    let documentsURL = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first!
    let destinationURL = documentsURL
      .appendingPathComponent("unarchived")
      .appendingPathComponent(url.absoluteString.md5())
    
    do {
      try? FileManager.default.removeItem(at: destinationURL)
      try FileManager.default.createDirectory(at: destinationURL, withIntermediateDirectories: true, attributes: nil)
      try FileManager.default.unzipItem(at: url, to: destinationURL)
      
      guard let bundleUrl = findBundlePath(path: destinationURL) else {
        throw "Bundle in archive not finded"
      }
      
      completion(true, bundleUrl,  nil)
    } catch {
      completion(false, nil, error)
    }
  }
  
  private static func findBundlePath(path: URL, contains: String = ".bundle") -> URL? {
    guard let contents = try? FileManager.default.contentsOfDirectory(atPath: path.path) else {
      print("Error reading directory contents at path: \(path.path)")
      return nil
    }
    
    for content in contents {
      let contentPath = path.appendingPathComponent(content)
      var isDirectory: ObjCBool = false
      
      
      if FileManager.default.fileExists(atPath: contentPath.path, isDirectory: &isDirectory) {
        print(content, isDirectory.boolValue)
        if isDirectory.boolValue {
          if let result = findBundlePath(path: contentPath, contains: contains) {
            return result
          }
        } else if content.contains(contains) {
          return contentPath
        }
      }
    }
    
    return nil
  }
}

extension String: Error {}
