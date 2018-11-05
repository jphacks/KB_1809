package studio.aquatan.plannap.worker

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import studio.aquatan.plannap.notification.NotificationController

class RetryPostPlanService : Service() {

    companion object {
        const val EXTRA_TITLE = "TITLE"
        const val EXTRA_UUID = "UUID"

        fun createPendingIntent(context: Context, title: String, uuid: String): PendingIntent {
            val intent = Intent(context, RetryPostPlanService::class.java).apply {
                putExtra(EXTRA_TITLE, title)
                putExtra(EXTRA_UUID, uuid)
            }

            return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        NotificationController(applicationContext).cancelPostPlanStatus()

        val data = Data.Builder().apply {
            putString(PostPlanWorker.KEY_TITLE, intent.getStringExtra(EXTRA_TITLE) ?: "unknown")
            putString(PostPlanWorker.KEY_OUTPUT_UUID,  intent.getStringExtra(EXTRA_UUID))
        }.build()

        val postRequest = OneTimeWorkRequest.Builder(PostPlanWorker::class.java)
            .setInputData(data)
            .build()

        WorkManager.getInstance().enqueue(postRequest)

        return START_NOT_STICKY
    }
}