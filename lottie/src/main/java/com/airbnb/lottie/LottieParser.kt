package com.airbnb.lottie

/**
 * Created by Eagle on 2017/5/13.
 */

import android.app.Activity
import android.content.Context
import android.util.Log
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.security.MessageDigest
import java.util.zip.ZipInputStream

/**
 * Created by PonyCui_Home on 16/6/18.
 */
class LottieParser(val context: Context, val cachePath: String) {

    interface OnParseCompletionListener {
        fun onComplete(jsonFile: File)
        fun onError()
    }

    fun parse(url: URL): File? {
        try {
            Log.i(javaClass.simpleName, "parse url [" + url.toString() + "]")
            if (cacheDir(cacheKey(url)).exists()) {
                return parse(null, cacheKey(url))
            }
            else {
                (url.openConnection() as? HttpURLConnection)?.let {
                    it.connectTimeout = 20 * 1000
                    it.requestMethod = "GET"
                    it.connect()
                    return parse(it.inputStream, cacheKey(url))
                }
            }
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, "parse url [" + url.toString() + "] failed!", e)
        }
        return null
    }

    fun parse(url: URL, callback: OnParseCompletionListener) {
        Thread(Runnable {
            parse(url)?.let {
                (context as? Activity)?.runOnUiThread {
                    callback.onComplete(it)
                }
                return@Runnable
            }
            (context as? Activity)?.runOnUiThread {
                callback.onError()
            }
        }).start()
    }

    fun parse(inputStream: InputStream?, cacheKey: String): File? {
        inputStream?.let {
            if (!cacheDir(cacheKey).exists()) {
                unzip(it, cacheKey)
            }
        }
        val cacheDir = File("$cachePath/$cacheKey/")
        return File(cacheDir, "data.json")
    }

    private fun cacheKey(str: String): String {
        val messageDigest = MessageDigest.getInstance("MD5")
        messageDigest.update(str.toByteArray(charset("UTF-8")))
        val digest = messageDigest.digest()
        val sb = StringBuffer()
        for (b in digest) {
            sb.append(String.format("%02x", b))
        }
        return sb.toString()
    }

    private fun cacheKey(url: URL): String {
        return cacheKey(url.toString())
    }

    private fun cacheDir(cacheKey: String): File {
        return File("$cachePath/$cacheKey/")
    }

    private fun unzip(inputStream: InputStream, cacheKey: String) {
        try {
            val cacheDir = this.cacheDir(cacheKey)
            cacheDir.mkdirs()
            val zipInputStream = ZipInputStream(BufferedInputStream(inputStream))
            while (true) {
                val zipItem = zipInputStream.nextEntry ?: break
                val file = File(cacheDir, zipItem.name)
                if (zipItem.isDirectory) {
                    file.mkdirs()
                } else {
                    val fileOutputStream = FileOutputStream(file)
                    val buff = ByteArray(2048)
                    while (true) {
                        val readBytes = zipInputStream.read(buff)
                        if (readBytes <= 0) {
                            break
                        }
                        fileOutputStream.write(buff, 0, readBytes)
                    }
                    fileOutputStream.close()
                }
                zipInputStream.closeEntry()
            }
            zipInputStream.close()
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, "unzip failed!", e)
        }
    }

}