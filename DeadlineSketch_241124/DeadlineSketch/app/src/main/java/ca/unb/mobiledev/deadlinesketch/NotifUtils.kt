package ca.unb.mobiledev.deadlinesketch

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import ca.unb.mobiledev.deadlinesketch.dao.NotificationDao
import ca.unb.mobiledev.deadlinesketch.entity.Notification
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Period
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar

object NotifUtils {
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun calculateTriggerTime(notif: Notification): Long {
//        var curDate = LocalDate.ofInstant(Calendar.getInstance().toInstant(), ZoneId.systemDefault())
        var notifDate = LocalDate.parse(notif.activation_date)
//        var curTime = LocalDateTime.ofInstant(Calendar.getInstance().toInstant(), ZoneId.systemDefault())
        var timeArray: List<String> = notif.activation_time.split(":")
        var hour = timeArray[0].toInt()
        var minute = timeArray[1].toInt()
        var time = LocalTime.of(hour,minute)
//
//        val numDays = Period.between(curDate,notifDate)
//        var numDaysPeriod = numDays.days
//        var activationTimeGap = (numDaysPeriod * 24 * 60 * 60 * 1000) + (hours * 60 * 60 * 1000) + (minutes * 60 * 1000)

        val currDateTime = LocalDateTime.now()
        var notifDateTime: LocalDateTime = LocalDateTime.of(notifDate, time)
        var duration = Duration.between(currDateTime,notifDateTime)
        return System.currentTimeMillis() + duration.toMillis()

        //return System.currentTimeMillis() + activationTimeGap
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun scheduleAlarm(alrmMngr: AlarmManager,
                      pendingIntent: PendingIntent?,
                      notificationWithPriority: NotificationDao.NotificationWithPriority,
                      triggerTime: Long) {
        val notif = notificationWithPriority.notification
        val priority = notificationWithPriority.priority
        val interval = notif.reaccuring_time.split(" ")[0].toInt()
        Log.i(TAG, "Interval: "+ interval+" days")
        val currentTime = LocalDateTime.now()
        var notifDate = LocalDate.parse(notif.activation_date)
        var timeArray: List<String> = notif.activation_time.split(":")
        var hour = timeArray[0].toInt()
        var minute = timeArray[1].toInt()
        var time = LocalTime.of(hour,minute)
        var notifDateTime: LocalDateTime = LocalDateTime.of(notifDate, time)
        var duration = Duration.between(currentTime,notifDateTime)

        try {
            if (pendingIntent != null) {
                if (duration.toMillis() < 0) {
                    scheduleLateAlarms(alrmMngr, pendingIntent, notificationWithPriority)
                }
                if (priority == "High" || priority == "Emergency") {
                    if (notif.isReacurring) {
                        alrmMngr.setRepeating(AlarmManager.RTC_WAKEUP, triggerTime, interval.toLong(), pendingIntent)
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            if (alrmMngr.canScheduleExactAlarms()) {
                                alrmMngr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
                            } else {
                                throw SecurityException("App does not have permission to set exact alarms")
                                }
                        } else {
                            alrmMngr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
                            }
                    }
                } else {
                    if (notif.isReacurring) {
                        alrmMngr.setInexactRepeating(AlarmManager.RTC_WAKEUP, triggerTime, interval.toLong(), pendingIntent)
                    } else {
                        alrmMngr.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
                        //May want to scale back to an even more flexible alarm in future for low priority deadlines
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("NotifUtils", "Cannot schedule exact alarms: ${e.message}")
            if (pendingIntent != null) {
                alrmMngr.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun scheduleLateAlarms(alrmMngr: AlarmManager,
                                   pendingIntent: PendingIntent,
                                   notificationWithPriority: NotificationDao.NotificationWithPriority) {
        val notif = notificationWithPriority.notification
        val priority = notificationWithPriority.priority
        val interval = notif.reaccuring_time.split(" ")[0].toInt()
        Log.i(TAG, "Interval: "+ interval+" days")
        val currentDateTime = LocalDateTime.now()
        val currentDate = LocalDate.now()
        val newNotifDate = currentDate.plusDays(interval.toLong())
        var timeArray: List<String> = notif.activation_time.split(":")
        var hour = timeArray[0].toInt()
        var minute = timeArray[1].toInt()
        var time = LocalTime.of(hour,minute)
        var newNotifDateTime: LocalDateTime = LocalDateTime.of(newNotifDate, time)
        var duration = Duration.between(currentDateTime,newNotifDateTime)

        try {
            if (priority == "High" || priority == "Emergency") {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (alrmMngr.canScheduleExactAlarms()) {
                        alrmMngr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent)
                        } else {
                        throw SecurityException("App does not have permission to set exact alarms")
                        }
                } else {
                    alrmMngr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent)
                    }
                if (notif.isReacurring) {
                    alrmMngr.setRepeating(AlarmManager.RTC_WAKEUP, duration.toMillis(), interval.toLong(), pendingIntent)
                }
            } else {
                alrmMngr.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent)
                if (notif.isReacurring) {
                    alrmMngr.setInexactRepeating(AlarmManager.RTC_WAKEUP, duration.toMillis(), interval.toLong(), pendingIntent)
                }
            }
        } catch (e: SecurityException) {
            Log.e("NotifUtils", "Cannot schedule exact alarms: ${e.message}")
            alrmMngr.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun requestRuntimePermissions(activity: MainActivity) {
        ActivityCompat.requestPermissions(
            activity, arrayOf(
                Manifest.permission.POST_NOTIFICATIONS
            ), Constants.NOTIFICATION_REQUEST_ID
        )
    }

    fun checkNotificationPermissions(activity: MainActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestRuntimePermissions(activity)
            }
        }
    }

    fun rescheduleAlarms(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        //ToDo Complete implementation of comprehensive alarm rescheduling
    }
}
