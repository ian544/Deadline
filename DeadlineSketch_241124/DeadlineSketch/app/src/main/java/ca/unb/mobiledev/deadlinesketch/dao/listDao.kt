package ca.unb.mobiledev.deadlinesketch.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ca.unb.mobiledev.deadlinesketch.entity.list

@Dao
interface listDao {
    @Query("SELECT * from list_table ORDER BY list_id ASC")
    fun listAllLists(): List<list>

    @Query("SELECT * FROM list_table WHERE list_id = :listID")
    fun listSingleList(listID: Int): List<list>

    @Query("SELECT * FROM list_table WHERE list_name = :listName")
    fun listSingleListName(listName: String): List<list>

    @Update(list::class)
    fun updateList(list: list)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertList(list: list)

    @Delete
    infix fun deleteList(list: list): Int

}