package com.example.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PhotoFileHelper {

    fun createNewPhotoUri(context: Context): Pair<File, Uri> {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "ORDEN_${timeStamp}.jpg"
        val storageDir = File(context.filesDir, "delivery_photos").apply {
            if (!exists()) mkdirs()
        }
        val photoFile = File(storageDir, fileName)
        val authority = "${context.packageName}.fileprovider"
        val photoUri = FileProvider.getUriForFile(context, authority, photoFile)
        return Pair(photoFile, photoUri)
    }

    fun copyUriToLocalStorage(context: Context, sourceUri: Uri): Pair<File, Uri>? {
        return try {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "ORDEN_GALERIA_${timeStamp}.jpg"
            val storageDir = File(context.filesDir, "delivery_photos").apply {
                if (!exists()) mkdirs()
            }
            val destinationFile = File(storageDir, fileName)

            context.contentResolver.openInputStream(sourceUri)?.use { inputStream: InputStream ->
                FileOutputStream(destinationFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }

            val authority = "${context.packageName}.fileprovider"
            val photoUri = FileProvider.getUriForFile(context, authority, destinationFile)
            Pair(destinationFile, photoUri)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getFormattedDateTime(timestamp: Long = System.currentTimeMillis()): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    fun getFormattedDateForSubject(timestamp: Long = System.currentTimeMillis()): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    fun generateDefaultOrderCode(timestamp: Long = System.currentTimeMillis()): String {
        val sdf = SimpleDateFormat("yyyyMMdd-HHmm", Locale.getDefault())
        return "ORD-${sdf.format(Date(timestamp))}"
    }
}
