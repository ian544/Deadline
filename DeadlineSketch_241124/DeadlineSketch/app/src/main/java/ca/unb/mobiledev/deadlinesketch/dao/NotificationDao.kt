package ca.unb.mobiledev.deadlinesketch.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ca.unb.mobiledev.deadlinesketch.entity.Notification

@Dao
interface NotificationDao {
    @Query("SELECT * from notification_table WHERE notification_table.task_id = :taskID")
    fun listAllNotificationsFromTask(taskID: Int): List<Notification>

    @Query("SELECT * from notification_table ")
    fun listAllNotifications(): List<Notification>

    @Query("""
        SELECT n.*, t.priority 
        FROM notification_table n
        JOIN task_table t ON n.task_id = t.task_id
        WHERE n.activation_date <= date('now','+7 days') AND n.scheduled = 0 AND n.disabled = 0""")
    fun listAllUnscheduledNotificationsWithPriority(): List<NotificationWithPriority>

    @Update(Notification::class)
    fun changeNotification(notification: Notification)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertNotification(notification: Notification)

    @Delete
    infix fun deleteNotification(notification: Notification): Int

    data class NotificationWithPriority(
        @Embedded val notification: Notification,
        val priority: String
    )

}