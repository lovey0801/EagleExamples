package com.airbnb.lottie

import android.content.Context
import android.graphics.BitmapFactory
import android.util.AttributeSet
import android.util.Log
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.net.URL

/**
 * Created by Eagle on 2017/5/13.
 */
class LottieAnimationExView : LottieAnimationView {
    val TAG = javaClass.simpleName

    constructor(context: Context) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    open fun setAnimation(url: URL) {
        val parser = LottieParser(context, LottieManager.cachePath ?: context.cacheDir.absolutePath)
        parser.parse(url, object : LottieParser.OnParseCompletionListener {
            override fun onComplete(jsonFile: File) {
                if (jsonFile.exists()) {
                    try {
                        FileInputStream(jsonFile)?.let {
                            val baos = ByteArrayOutputStream()
                            it.copyTo(baos)
                            JSONObject(String(baos.toByteArray()))?.let {
                                post {
                                    setImageAssetDelegate {
                                        image -> try {
                                            BitmapFactory.decodeFile(jsonFile.parent + "/images/" + image.fileName)
                                        } catch (e: Exception) {
                                            Log.e(TAG, "fetchBitmap image [" + image.fileName + "] error!", e)
                                            null
                                        }
                                    }
                                    setAnimation(it)
                                }
                            }
                            it.close()
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "parse url [" + url.toString() + "] error!", e)
                    }
                }
            }

            override fun onError() {
            }
        })
    }
}