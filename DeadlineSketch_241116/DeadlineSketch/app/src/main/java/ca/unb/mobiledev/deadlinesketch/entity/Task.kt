package ca.unb.mobiledev.deadlinesketch.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(tableName = "task_table", foreignKeys = [ForeignKey(list::class, arrayOf("list_id"), arrayOf("list_id"), ForeignKey.CASCADE, ForeignKey.CASCADE)])
class Task {
    @PrimaryKey(autoGenerate = true)
    var task_id: Int = 0
    var list_id: Int = 0
    var title: String = ""
    var due_date: Long = 0
    var priority: Int = 0
    var activate_time: Long = 0
    var description: String = ""
}