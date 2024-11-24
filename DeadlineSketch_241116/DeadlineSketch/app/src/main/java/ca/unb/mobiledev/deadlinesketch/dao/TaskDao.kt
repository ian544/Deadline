package ca.unb.mobiledev.deadlinesketch.dao
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ca.unb.mobiledev.deadlinesketch.entity.Task

@Dao
interface TaskDao {
    @Query("SELECT * from task_table WHERE task_table.list_id = :listID")
    fun listAllTasksFromList(listID: Int): List<Task>

    @Query("SELECT * from task_table ORDER BY due_date LIMIT 15")
    fun listAllTasks(): List<Task>

    @Query("SELECT * from task_table WHERE task_table.task_id = :taskID")
    fun listSingleTask(taskID: Int): List<Task>

    @Update(Task::class)
    fun changeTask(task: Task)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTask(task: Task)

    @Delete
    infix fun deleteTask(task: Task): Int

}