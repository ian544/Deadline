package mobiledev.unb.ca.lab4skeleton

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import mobiledev.unb.ca.lab4skeleton.Constants.NOTIFICATION_CHANNEL_ID

class AlarmReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        Log.i(TAG, "it worked")
        handleNotification(context)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun handleNotification(context: Context) {
        lateinit var builder: NotificationCompat.Builder
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "This is an Example channel Name", NotificationManager.IMPORTANCE_HIGH
        )
        channel.description = "This is an example channel"
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        val intt = Intent(context, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(context, 2, intt, PendingIntent.FLAG_IMMUTABLE)
        builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("This is an example Notification")
            .setContentText("This is its body text")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            // Set the intent that fires when the user taps the notification.
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)


        // Show the notification
        with(NotificationManagerCompat.from(context)) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(Constants.NOTIFICATION_REQUEST_ID, builder.build())
        }
    }

}