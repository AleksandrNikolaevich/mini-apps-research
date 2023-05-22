import UIKit
import React

class MiniAppView : UIView {
  @objc var source: [String : String] = [:] {
    didSet {
      print(source)
      guard
        let url = source["url"],
        let moduleName = source["moduleName"]
      else { return }
      runApp(url: url, moduleName: moduleName)
    }
  }
  
  @objc var onStartLoad: RCTDirectEventBlock?
  @objc var onEndLoad: RCTDirectEventBlock?
  @objc var onError: RCTDirectEventBlock?
  
  
  func dispatch() {
    print("dispatch")
  }
  
  private func runApp(url: String, moduleName: String) {
    onStartLoad?(nil)
    
    BundleUtils.download(from: url) { [weak self] path in
      self?.createReactInstance(bundlePath: path, moduleName: moduleName)
    }
  }
  
  private func createReactInstance(bundlePath: URL, moduleName: String) {
    let rootView = RCTRootView(
      bundleURL: bundlePath,
      moduleName: moduleName,
      initialProperties: [:],
      launchOptions: [:]
    )
    
    self.addSubview(rootView)
    
    rootView.translatesAutoresizingMaskIntoConstraints = false
    rootView.widthAnchor.constraint(equalTo: self.widthAnchor).isActive = true
    rootView.heightAnchor.constraint(equalTo: self.heightAnchor).isActive = true
    
    
    self.onEndLoad?(nil)
  }
}
