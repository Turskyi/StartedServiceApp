package io.github.turskyi.startedserviceapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.JobIntentService
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import java.lang.Thread.sleep

class DelayedMessageService : JobIntentService() {

    companion object {
        const val EXTRA_MESSAGE = "message"
        const val CHANNEL_ID = "CHANNEL_ID"
        const val NOTIFICATION_ID = 5453
        private const val JOB_ID = 2
    }

    fun enqueueWork(context: Context, intent: Intent) {
        enqueueWork(context, DelayedMessageService::class.java, JOB_ID, intent)
    }

    override fun onHandleWork(intent: Intent) {
        synchronized(this) {
            try {
                sleep(10000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        val text = intent.getStringExtra(EXTRA_MESSAGE)
        showText(text)
    }

    private fun createNotificationChannel() {
        /* Create the NotificationChannel, but only on API 26+ because
         the NotificationChannel class is new and not in the support library */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            /* Register the channel with the system */
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showText(text: String?) {
        createNotificationChannel()
        /* Create a notification builder */
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(getString(R.string.question))
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(longArrayOf(0, 1000))
            .setAutoCancel(true)

        /* Create an action */
        val actionIntent = Intent(this, MainActivity::class.java)
        val stackBuilder: TaskStackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addParentStack(MainActivity::class.java)
        stackBuilder.addNextIntent(actionIntent)
        val actionPendingIntent: PendingIntent? = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

// another option to call the required screen on notification clicked without stack builder
//        val actionPendingIntent = PendingIntent.getActivity(
//                this,
//                0,
//                actionIntent, PendingIntent.FLAG_UPDATE_CURRENT
//        )

        builder.setContentIntent(actionPendingIntent)

        /* Issue the notification */
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }
}
