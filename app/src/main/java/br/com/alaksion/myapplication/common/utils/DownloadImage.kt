package br.com.alaksion.myapplication.common.utils

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.util.*

/**
 * Try to download image from  [url] , returns false if download failed and true if download succeeded
 *
 * @param[url] url that will be used to download the image
 *
 * **/
suspend fun Context.downloadImage(url: String) {
    val parsedUrl = URL(url)
    val result = CoroutineScope(Dispatchers.IO).async {
        parsedUrl.mapToBitmap()
    }
    Toast.makeText(this@downloadImage, "Downloading image...", Toast.LENGTH_SHORT).show()

    withContext(Dispatchers.IO) {
        val bitmap = result.await()
        bitmap?.saveToInternalStorage(this@downloadImage)
    }
}

fun URL.mapToBitmap(): Bitmap? {
    return try {
        BitmapFactory.decodeStream(openStream())
    } catch (e: Exception) {
        null
    }
}

fun Bitmap.saveToInternalStorage(context: Context) {
    val wrapper = ContextWrapper(context)
    val file = wrapper.getDir("images", Context.MODE_APPEND)
    val savedFile = File(file, "${UUID.randomUUID()}.jpg")

    try {
        val stream = FileOutputStream(savedFile)
        compress(Bitmap.CompressFormat.JPEG, 100, stream)
        stream.flush()
        stream.close()
        Uri.parse(file.absolutePath)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
