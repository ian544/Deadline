package ca.unb.mobiledev.deadlinesketch.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "task_table", foreignKeys = [ForeignKey(list::class, arrayOf("list_id"), arrayOf("list_id"), ForeignKey.CASCADE, ForeignKey.CASCADE)])
class Task {
    @PrimaryKey(autoGenerate = true)
    var task_id: Int = 0
    var list_id: Int = 0
    var title: String = ""
    var due_date: String = ""
    var priority: String = ""
    var activate_time: String = ""
    var description: String = ""
    var status: String = ""
}