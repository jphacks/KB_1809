package studio.aquatan.plannap.worker

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.squareup.moshi.Moshi
import kotlinx.coroutines.experimental.runBlocking
import studio.aquatan.plannap.Plannap
import studio.aquatan.plannap.data.PlanRepository
import studio.aquatan.plannap.data.adapter.UriJsonAdapter
import studio.aquatan.plannap.data.model.EditablePlan
import studio.aquatan.plannap.data.model.EditablePlanJsonAdapter
import studio.aquatan.plannap.data.model.PostPlan
import studio.aquatan.plannap.data.model.PostSpot
import studio.aquatan.plannap.notification.NotificationController
import studio.aquatan.plannap.util.calcScaleWidthHeight
import studio.aquatan.plannap.util.mapParallel
import studio.aquatan.plannap.util.rotateImageIfRequired
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import javax.inject.Inject

class PostPlanWorker(
    context: Context,
    workerParameters: WorkerParameters
) : Worker(context, workerParameters) {

    companion object {
        private const val TAG = "PostPlanWorker"
        private const val MAX_IMAGE_SIZE = 1920.0

        const val KEY_TITLE = "TITLE"
        const val KEY_OUTPUT_UUID = "OUTPUT_UUID"
    }

    @Inject
    lateinit var planRepository: PlanRepository

    private val editablePlanJsonAdapter: EditablePlanJsonAdapter by lazy {
        val moshi = Moshi.Builder()
            .add(UriJsonAdapter())
            .build()

        return@lazy EditablePlanJsonAdapter(moshi)
    }

    private val notification = NotificationController(context)

    override fun doWork(): Result {
        (applicationContext as Plannap).component.inject(this)

        val title = inputData.getString(KEY_TITLE) ?: "unknown"

        notification.makePostPlanStatus(title, PostStatus.LOADING)

        val uuid = inputData.getString(KEY_OUTPUT_UUID)
        val dir = File(applicationContext.filesDir, PlanRepository.OUTPUT_PATH)
        val file = File(dir, "$uuid.json")

        var input: FileInputStream? = null
        var isSuccess = false
        try {
            val bytes = ByteArray(file.length().toInt())
            input = FileInputStream(file)
            input.read(bytes)

            val editablePlan = editablePlanJsonAdapter.fromJson(String(bytes)) ?: throw IllegalArgumentException()

            notification.makePostPlanStatus(title, PostStatus.POSTING)

            isSuccess = runBlocking { planRepository.postPlan(editablePlan.asPostPlan()).await() }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to work", e)
        } finally {
            input?.close()
        }

        if (!isSuccess) {
            notification.makePostPlanStatus(title, PostStatus.FAILED)
            return Result.FAILURE
        }

        notification.makePostPlanStatus(title, PostStatus.SUCCESS)
        file.delete()

        return Result.SUCCESS
    }

    private fun EditablePlan.asPostPlan(): PostPlan {
        val postSpotList = spotList.mapParallel {
            val image = it.imageUri?.toBitmap() ?: throw NullPointerException("imageUri is null")

            val out = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.JPEG, 100, out)
            val bytes = out.toByteArray()
            out.close()

            return@mapParallel PostSpot(
                it.name,
                it.note,
                Base64.encodeToString(bytes, Base64.DEFAULT),
                it.lat,
                it.long
            )
        }

        return PostPlan(name, price, duration, note, postSpotList)
    }

    private fun Uri.toBitmap(): Bitmap? {
        try {
            val parcelFile = applicationContext.contentResolver.openFileDescriptor(this, "r") ?: return null

            val option = BitmapFactory.Options().apply {
                inPreferredConfig = Bitmap.Config.RGB_565
            }

            val image = BitmapFactory.decodeFileDescriptor(parcelFile.fileDescriptor, null, option)
            parcelFile.close()

            val (width, height) = image.calcScaleWidthHeight(MAX_IMAGE_SIZE, MAX_IMAGE_SIZE)
            val scaledImage = Bitmap.createScaledBitmap(image, width, height, true)
            image.recycle()

            return scaledImage.rotateImageIfRequired(this, applicationContext.contentResolver)
        } catch (e: IOException) {
            Log.e(javaClass.simpleName, "Failed to get Bitmap", e)
        }

        return null
    }
}