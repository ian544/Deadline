package ca.unb.mobiledev.deadlinesketch.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey


@Entity(tableName = "notification_table", foreignKeys = [ForeignKey(Task::class, arrayOf("task_id"), arrayOf("task_id"), ForeignKey.CASCADE, ForeignKey.CASCADE)])
class Notification {
    @PrimaryKey(autoGenerate = true)
    var notification_id: Int = 0
    var task_id: Int = 0
    var notification_name: String = ""
    var notification_description: String =""
    var activation_time: String = ""
    var activation_date: String = ""
    var isReacurring: Boolean = false
    var reaccuring_time: String = ""

}