package com.mainapp

import android.content.Context
import android.util.Log
import com.facebook.react.bridge.UiThreadUtil
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.math.BigInteger
import java.net.URL
import java.security.MessageDigest
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

object BundleUtils {
    fun download(context: Context, url: String, result: (path: String) -> Unit) {
        Thread {
            val documentsDir = context.filesDir
            val fileName = getFileNameByUrl(url)
            var file = File(documentsDir, fileName)

            safeDelete(file)

            if (!file.exists()) {

                val inputStream = URL(url).openStream()
                val outputStream = FileOutputStream(file)

                inputStream.use { input ->
                    outputStream.use { output ->
                        input.copyTo(output)
                    }
                }
            }

            if (url.contains(".zip")) {
                val zipFile = File(documentsDir, "$fileName.zip")
                safeDelete(zipFile)
                file.renameTo(zipFile)
                val destinationDirectory = File(documentsDir, fileName)
                unzip(zipFile, destinationDirectory)
                readDirectory(File(documentsDir, ""))
                file = findBundleFile(destinationDirectory)!!
            }

            UiThreadUtil.runOnUiThread {
                result.invoke(file.path)
            }
        }.start()
    }

    private fun findBundleFile(path: File, contains: String = ".bundle"): File? {
        val files = path.listFiles()
        if (files != null) {
            for (file in files) {
                if (file.isDirectory) {
                    val foundFile = findBundleFile(file, contains)
                    if (foundFile != null) {
                        return foundFile
                    }
                } else if (file.name.contains(contains)) {
                    return file
                }
            }
        }
        return null
    }

    private fun safeDelete(fileOrDirectory: File) {
        if (fileOrDirectory.isDirectory) {
            for (child in fileOrDirectory.listFiles()!!) {
                safeDelete(child)
            }
        }
        fileOrDirectory.delete()
    }

    private fun readDirectory(path: File) {
        val files = path.listFiles()
        if (files != null) {
            for (file in files) {
                if (file.isDirectory) {
                    readDirectory(file)
                } else  {
                    Log.d("MiniApp", file.path)
                }
            }
        }
    }

    private fun getFileNameByUrl(url: String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(url.toByteArray())).toString(16).padStart(32, '0')
    }

    private fun unzip(zipFile: File, destinationDirectory: File) {
        // Create a buffer for reading the ZIP file
        val buffer = ByteArray(1024)

        // Create a ZipInputStream for reading the ZIP file
        val zipInputStream = ZipInputStream(FileInputStream(zipFile))

        // Iterate over each entry in the ZIP file
        var entry: ZipEntry? = zipInputStream.nextEntry
        while (entry != null) {
            // Extract the entry to the destination directory
            val outputFile = File(destinationDirectory, entry.name)
            if (entry.isDirectory) {
                Log.d("MIniApp", "create dir: ${entry.name}")
                outputFile.mkdirs()
            } else {
                // Create parent directories if necessary
                val parent = outputFile.parentFile
                if (!parent.exists()) {
                    parent.mkdirs()
                }
                Log.d("MIniApp", "copy file to: ${entry.name}")
                // Write the file contents to disk
                outputFile.outputStream().use { outputStream ->
                    var len = zipInputStream.read(buffer)
                    while (len > 0) {
                        outputStream.write(buffer, 0, len)
                        len = zipInputStream.read(buffer)
                    }
                }
            }

            // Move to the next entry
            entry = zipInputStream.nextEntry
        }

        // Close the ZipInputStream
        zipInputStream.closeEntry()
        zipInputStream.close()
    }
}