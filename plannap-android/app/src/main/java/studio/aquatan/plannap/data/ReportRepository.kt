package studio.aquatan.plannap.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import studio.aquatan.plannap.Session
import studio.aquatan.plannap.data.api.ReportService
import studio.aquatan.plannap.data.model.PostReport
import studio.aquatan.plannap.data.model.Report
import studio.aquatan.plannap.util.calcScaleWidthHeight
import java.io.ByteArrayOutputStream
import java.io.IOException

class ReportRepository(context: Context, session: Session) : BaseRepository(session) {

    private val appContext = context.applicationContext

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://plannap.aquatan.studio")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    private val service = retrofit.create(ReportService::class.java)

    fun getReportList(planId: Int): LiveData<List<Report>> {
        val result = MutableLiveData<List<Report>>()

        GlobalScope.launch {
            try {
                val response = service.getReports(planId).execute()
                result.postValue(response.body() ?: emptyList())
            } catch (e: Exception) {
                Log.e(javaClass.simpleName, "Failed to fetch getReports", e)
            }
        }

        return result
    }

    fun postReport(planId: Long, text: String, imageUri: Uri) =
        GlobalScope.async {
            val image = imageUri.toBitmap() ?: throw NullPointerException("imageUri is null")

            val out = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.JPEG, 100, out)
            val bytes = out.toByteArray()
            out.close()

            val postReport = PostReport(image = Base64.encodeToString(bytes, Base64.DEFAULT), text = text)
            try {
                val response = service.postReport(planId, postReport).execute()
                return@async Pair(response.code() == 201, response.message())
            } catch (e: Exception) {
                Log.e(javaClass.simpleName, "Failed to fetch postReport", e)
            }

            return@async Pair(false, "Unknown error")
        }

    private fun Uri.toBitmap(): Bitmap? {
        val MAX_IMAGE_SIZE = 1920.0
        try {
            val parcelFile = appContext.contentResolver.openFileDescriptor(this, "r") ?: return null

            val option = BitmapFactory.Options().apply {
                inPreferredConfig = Bitmap.Config.RGB_565
            }

            val image = BitmapFactory.decodeFileDescriptor(parcelFile.fileDescriptor, null, option)
            parcelFile.close()

            val (width, height) = image.calcScaleWidthHeight(MAX_IMAGE_SIZE, MAX_IMAGE_SIZE)

            return Bitmap.createScaledBitmap(image, width, height, true)
        } catch (e: IOException) {
            Log.e(javaClass.simpleName, "Failed to get Bitmap", e)
        }

        return null
    }
}

