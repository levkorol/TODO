package com.levkorol.todo.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.FileProvider
import com.jakewharton.threetenabp.AndroidThreeTen
import com.levkorol.todo.ui.note.AddNoteFragment
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream
import java.util.*

fun convertBitmapToByteArray(bitmap: Bitmap): ByteArray {
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream)
    return stream.toByteArray()
}

fun convertToString(array: ByteArray): String {
    return Base64.encodeToString(array, Base64.NO_WRAP or Base64.URL_SAFE)
}

fun convertToByteArrayView(string: String?): ByteArray? {
    return if (string != null) {
        Base64.decode(string, Base64.NO_WRAP or Base64.URL_SAFE)
    } else {
        null
    }
}

fun convertByteArrayToBitmapView(byteArray: ByteArray?): Bitmap? {
    return if (byteArray != null) {
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    } else {
        null
    }
}

fun decodeUriToBitmap(mContext: Context, sendUri: Uri): Bitmap? {
    var getBitmap: Bitmap? = null
    try {
        val imageStream: InputStream
        try {
            imageStream = mContext.contentResolver.openInputStream(sendUri)!!
            getBitmap = BitmapFactory.decodeStream(imageStream)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

    } catch (e: Exception) {
        e.printStackTrace()
    }

    return getBitmap
}

object PictureUtils {

    fun getScaledBitmap(path: String, activity: Activity): Bitmap {
        val size = Point()
        activity.windowManager.defaultDisplay.getSize(size)
        return getScaledBitmap(path, size.x, size.y)
    }

    fun getScaledBitmap(path: String, destWidth: Int, destHeight: Int): Bitmap {
        var options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options)

        val srcWidth = options.outWidth.toFloat()
        val srcHeight = options.outHeight.toFloat()

        var inSampleSize = 1
        if (srcHeight > destHeight || srcWidth > destWidth) {
            val heightScale = srcHeight / destHeight
            val widthScale = srcWidth / destWidth
            inSampleSize = Math.round(if (heightScale > widthScale) heightScale else widthScale)
        }

        options = BitmapFactory.Options()
        options.inSampleSize = inSampleSize

        return BitmapFactory.decodeFile(path, options)
    }
}

