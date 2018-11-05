package studio.aquatan.plannap.util

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.util.Log
import androidx.exifinterface.media.ExifInterface
import java.io.InputStream

fun Bitmap.rotateImageIfRequired(uri: Uri, contentResolver: ContentResolver): Bitmap {

    var stream: InputStream? = null
    try {
        stream = contentResolver.openInputStream(uri)

        val exifInterface = ExifInterface(stream)
        val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> return rotateImage(90)
            ExifInterface.ORIENTATION_ROTATE_180 -> return rotateImage(180)
            ExifInterface.ORIENTATION_ROTATE_270 -> return rotateImage(270)
        }
    } catch (e: Exception) {
        Log.e("BitmapHelper", "failed to rotate", e)
    } finally {
        stream?.close()
    }

    return this
}

private fun Bitmap.rotateImage(degree: Int): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(degree.toFloat())
    val rotatedImage = Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    recycle()
    return rotatedImage
}