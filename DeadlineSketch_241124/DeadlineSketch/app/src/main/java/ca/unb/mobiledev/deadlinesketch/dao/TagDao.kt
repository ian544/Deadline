package ca.unb.mobiledev.deadlinesketch.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ca.unb.mobiledev.deadlinesketch.entity.Tag

@Dao
interface TagDao {
    @Query("SELECT * from tag_table WHERE tag_table.task_id = :taskID")
    fun listAllTagsFromTask(taskID: Int): List<Tag>

    @Query("SELECT * from task_table, tag_table WHERE tag_name = :name")
    fun listAllTagsFromName(name: String): List<Tag>

    @Update(Tag::class)
    fun changeTag(task: Tag)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTag(task: Tag)

    @Delete
    infix fun deleteTag(task: Tag): Int

}