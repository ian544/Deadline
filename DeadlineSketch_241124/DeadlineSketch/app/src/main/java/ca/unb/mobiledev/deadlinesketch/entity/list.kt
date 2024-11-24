package ca.unb.mobiledev.deadlinesketch.entity


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "list_table")
class list {
    @PrimaryKey(autoGenerate = true)
    var list_id: Int = 0
    var list_name: String = ""
    var list_left: Int = 0
    var list_right: Int = 0

}