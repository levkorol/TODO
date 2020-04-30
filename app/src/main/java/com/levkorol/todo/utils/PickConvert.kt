package com.levkorol.todo.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

  fun convertBitmapToByteArray(bitmap: Bitmap): ByteArray {
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream)
    return stream.toByteArray()
  }

  fun convertToString(array: ByteArray): String {
    val result = android.util.Base64.encodeToString(array, Base64.NO_WRAP or Base64.URL_SAFE)
    return result
  }

  fun convertToByteArrayView(string: String?): ByteArray? {
    if (string != null) {
        return Base64.decode(string, Base64.NO_WRAP or Base64.URL_SAFE)
    } else {
        return null
    }
  }

  fun convertByteArrayToBitmapView(byteArray: ByteArray?): Bitmap? {
    if (byteArray != null) {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    } else {
        return null
    }
  }