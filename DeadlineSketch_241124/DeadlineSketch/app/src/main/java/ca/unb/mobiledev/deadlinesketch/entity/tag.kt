package ca.unb.mobiledev.deadlinesketch.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "tag_table", foreignKeys = [ForeignKey(Task::class, arrayOf("task_id"), arrayOf("task_id"), ForeignKey.CASCADE, ForeignKey.CASCADE)])
class Tag {
    @PrimaryKey(autoGenerate = true)
    var tag_id: Int = 0
    var task_id: Int = 0
    var tag_name: String = ""
}