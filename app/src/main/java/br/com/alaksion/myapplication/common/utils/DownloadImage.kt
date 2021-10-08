package br.com.alaksion.myapplication.common.utils

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import java.io.File
import java.util.*

// TODO -> Make this work later
fun Context.downloadImage(uri: String) {
    try {
        val filename = UUID.randomUUID().toString()
        val manager = this.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val parsedUri = Uri.parse(uri)
        val request = DownloadManager.Request(parsedUri)
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
            .setAllowedOverRoaming(false)
            .setTitle(filename)
            .setMimeType("image/jpeg")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_PICTURES,
                File.separator + filename + "jpg"
            )
        manager.enqueue(request)
        Toast.makeText(this, "Downloading image...", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(
            this,
            "An error occurred and the image could not be downloaded",
            Toast.LENGTH_SHORT
        ).show()
        e.printStackTrace()
    }

}
