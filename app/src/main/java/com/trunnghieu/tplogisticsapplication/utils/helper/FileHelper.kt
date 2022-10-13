package com.trunnghieu.tplogisticsapplication.utils.helper

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.webkit.MimeTypeMap
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*

object FileHelper {

    /**
     * Get mime type from file
     */
    fun getMimeType(context: Context, uri: Uri?): String? {
        var mimeType: String? = null
        try {
            mimeType = if (uri!!.scheme == ContentResolver.SCHEME_CONTENT) {
                val cr = context.contentResolver
                cr.getType(uri)
            } else {
                val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.lowercase(Locale.ROOT)
                )
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
        return mimeType
    }

    /**
     * Get mimetype from url
     */
    fun getMimeTypeFromUrl(url: String?): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        if (extension != null) {
            when (extension) {
                "js" -> {
                    return "text/javascript"
                }
                "woff" -> {
                    return "application/font-woff"
                }
                "woff2" -> {
                    return "application/font-woff2"
                }
                "ttf" -> {
                    return "application/x-font-ttf"
                }
                "eot" -> {
                    return "application/vnd.ms-fontobject"
                }
                "svg" -> {
                    return "image/svg+xml"
                }
                else -> type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
            }
        }
        return type
    }

    /**
     * Convert bitmap to file
     */
    fun bitmapToFile(
        context: Context,
        bitmap: Bitmap
    ): File? { // File name like "image.png"
        //create a file to write bitmap data
        var file: File? = null
        return try {
            file = File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!.path +
                        File.separator +
                        System.currentTimeMillis() + ".png"
            )
            file.createNewFile()

            //Convert bitmap to byte array
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos) // YOU can also save it in JPEG
            val bitmapData = bos.toByteArray()

            //write the bytes in file
            val fos = FileOutputStream(file)
            fos.write(bitmapData)
            fos.flush()
            fos.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            file // it will return null
        }
    }
}