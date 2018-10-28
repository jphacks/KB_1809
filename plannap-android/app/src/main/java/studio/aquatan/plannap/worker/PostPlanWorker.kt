package studio.aquatan.plannap.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.squareup.moshi.Moshi
import dagger.android.AndroidInjection
import kotlinx.coroutines.experimental.runBlocking
import studio.aquatan.plannap.Plannap
import studio.aquatan.plannap.data.PlanRepository
import studio.aquatan.plannap.data.model.PostPlanJsonAdapter
import java.io.File
import java.io.FileInputStream
import javax.inject.Inject

class PostPlanWorker(
    context: Context,
    workerParameters: WorkerParameters
) : Worker(context, workerParameters) {

    companion object {
        private const val TAG = "PostPlanWorker"

        const val KEY_OUTPUT_UUID = "OUTPUT_UUID"
    }

    @Inject
    lateinit var planRepository: PlanRepository

    private val postPlanJsonAdapter: PostPlanJsonAdapter by lazy {
        val moshi = Moshi.Builder()
            .build()

        return@lazy PostPlanJsonAdapter(moshi)
    }

    override fun doWork(): Result {
        (applicationContext as Plannap).component.inject(this)

        val uuid = inputData.getString(KEY_OUTPUT_UUID)
        val dir = File(applicationContext.filesDir, PlanRepository.OUTPUT_PATH)
        val file = File(dir, "$uuid.json")

        var input: FileInputStream? = null
        try {
            val bytes = ByteArray(file.length().toInt())
            input = FileInputStream(file)
            input.read(bytes)

            val postPlan = postPlanJsonAdapter.fromJson(String(bytes)) ?: throw IllegalArgumentException()

            runBlocking {
                planRepository.postPlan(postPlan)
            }

        } catch (e: Exception) {
            Log.e(TAG, "exception", e)
            return Result.FAILURE
        } finally {
            input?.close()
        }

        file.delete()
        return Result.SUCCESS
    }

}