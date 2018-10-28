package studio.aquatan.plannap.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import studio.aquatan.plannap.R
import studio.aquatan.plannap.worker.PostStatus


class NotificationController(context: Context): ContextWrapper(context) {

    companion object {
        private const val POST_CHANNEL_ID = "10"
        private const val POST_CHANNEL_NAME = "投稿"
    }

    init {
        // Make a channel if necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val postChannel = NotificationChannel(POST_CHANNEL_ID, POST_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            postChannel.description = "投稿状態を表示します"

            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(postChannel)
        }
    }

    fun makePostPlanStatus(title: String, status: PostStatus) {
        val builder = NotificationCompat.Builder(this, POST_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_send_black_24dp)
            .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
            .setContentTitle(title)
            .setContentText(getString(status.resId))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(LongArray(0))

        NotificationManagerCompat.from(this).notify(10, builder.build())
    }
}