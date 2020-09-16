
package com.appyhigh.mylibrary.graphic

import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Base64
import androidx.annotation.IntRange
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


fun Bitmap.convertPngToByteArray(): ByteArray? {
    return this.convertToByteArray(Bitmap.CompressFormat.PNG, 80)
}

fun Bitmap.convertJpegToByteArray(): ByteArray? {
    return this.convertToByteArray(Bitmap.CompressFormat.JPEG, 80)
}

fun Bitmap.convertWebPToByteArray(): ByteArray? {
    return this.convertToByteArray(Bitmap.CompressFormat.WEBP, 80)
}

fun Bitmap.convertToByteArray(format: Bitmap.CompressFormat, @IntRange(from = 0, to = 100) quality: Int): ByteArray? {
    val byteArrayOutputStream = ByteArrayOutputStream()
    this.compress(format, quality, byteArrayOutputStream)
    return byteArrayOutputStream.toByteArray()
}


fun Bitmap.toBase64(compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG): String {
    val result: String
    val baos = ByteArrayOutputStream()
    compress(compressFormat, 100, baos)
    baos.flush()
    baos.close()
    val bitmapBytes = baos.toByteArray()
    result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT)
    baos.flush()
    baos.close()
    return result
}

fun Bitmap.resize(w: Number, h: Number): Bitmap {
    val width = width
    val height = height
    val scaleWidth = w.toFloat() / width
    val scaleHeight = h.toFloat() / height
    val matrix = Matrix()
    matrix.postScale(scaleWidth, scaleHeight)
    if (width > 0 && height > 0) {
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }
    return this
}

fun Bitmap.saveFile(path: String, compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG) {
    val f = File(path)
    if (!f.exists()) {
        f.createNewFile()
    }
    val stream = FileOutputStream(f)
    compress(compressFormat, 100, stream)
    stream.flush()
    stream.close()
}