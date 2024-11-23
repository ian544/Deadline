package ca.unb.mobiledev.deadlinesketch.Repo

import android.app.Application
import android.content.Context
import ca.unb.mobiledev.deadlinesketch.dao.NotificationDao
import ca.unb.mobiledev.deadlinesketch.dao.TagDao
import ca.unb.mobiledev.deadlinesketch.dao.TaskDao
import ca.unb.mobiledev.deadlinesketch.dao.listDao
import ca.unb.mobiledev.deadlinesketch.entity.Notification
import ca.unb.mobiledev.deadlinesketch.entity.Tag
import ca.unb.mobiledev.deadlinesketch.entity.Task
import ca.unb.mobiledev.deadlinesketch.entity.list
import ca.unb.mobiledev.deadlinesketch.db.AppDatabase
import ca.unb.mobiledev.deadlinesketch.db.AppDatabase.Companion.getDatabase
import java.util.concurrent.Callable
import java.util.concurrent.ExecutionException
import java.util.concurrent.Future

class dbRepo(context: Context) {
    private val db = AppDatabase.getDatabase(context)
    private val list_dao: listDao = getDatabase(context).listDao()
    private val notif_Dao: NotificationDao = getDatabase(context).notificationDao()
    private val tag_Dao: TagDao = getDatabase(context).tagDao()
    private val task_Dao: TaskDao = getDatabase(context).taskDao()

    fun insertList(lName: String){
        var list = list()
        list.list_name = lName
        AppDatabase.databaseWriterExecutor.execute { list_dao.insertList(list) }
    }

    fun insertNotif(nName: String, nDescrip: String, taskID: Int, actTime: Long, isRec: Boolean, recTime: Long){
        var notif = Notification()
        notif.notification_name = nName
        notif.notification_description = nDescrip
        notif.task_id = taskID
        notif.activation_time = actTime
        notif.reaccuring_time = recTime
        notif.isReacurring = isRec
        AppDatabase.databaseWriterExecutor.execute { notif_Dao.insertNotification(notif) }
    }

    fun insertTask(lID: Int, tName: String, tDescrip: String, dueDate: Long, actDate: Long, prio: Int){
        var task = Task()
        task.list_id = lID
        task.title = tName
        task.description = tDescrip
        task.due_date = dueDate
        task.activate_time = actDate
        task.priority = prio
        AppDatabase.databaseWriterExecutor.execute { task_Dao.insertTask(task) }
    }

    fun insertTag(tName: String, tID: Int){
        var tag = Tag()
        tag.tag_name = tName
        tag.task_id = tID;
        AppDatabase.databaseWriterExecutor.execute { tag_Dao.insertTag(tag) }
    }

    fun getList(): List<list>{
        val dataReadFuture: Future<List<list>> = AppDatabase.databaseWriterExecutor.submit(
            Callable {
                list_dao.listAllLists()
            })
        return try {
            while (!dataReadFuture.isDone) {
            }
            dataReadFuture.get()
        }catch(e: ExecutionException){
            emptyList()
        }
        catch(e: InterruptedException){
            emptyList()
        }
    }

    fun getTaskList(list_id: Int): List<Task>{
        val dataReadFuture: Future<List<Task>> = AppDatabase.databaseWriterExecutor.submit(
            Callable {
                task_Dao.listAllTasksFromList(list_id)
            })
        return try {
            while (!dataReadFuture.isDone) {
            }
            dataReadFuture.get()
        }catch(e: ExecutionException){
            emptyList()
        }
        catch(e: InterruptedException){
            emptyList()
        }
    }
    fun getTaskAll(): List<Task>{
        val dataReadFuture: Future<List<Task>> = AppDatabase.databaseWriterExecutor.submit(
            Callable {
                task_Dao.listAllTasks()
            })
        return try {
            while (!dataReadFuture.isDone) {
            }
            dataReadFuture.get()
        }catch(e: ExecutionException){
            emptyList()
        }
        catch(e: InterruptedException){
            emptyList()
        }
    }

    fun getNotif(task_id: Int): List<Notification>{
        val dataReadFuture: Future<List<Notification>> = AppDatabase.databaseWriterExecutor.submit(
            Callable {
                notif_Dao.listAllNotificationsFromTask(task_id)
            })
        return try {
            while (!dataReadFuture.isDone) {
            }
            dataReadFuture.get()
        }catch(e: ExecutionException){
            emptyList()
        }
        catch(e: InterruptedException){
            emptyList()
        }
    }

    fun getTagsTask(name: String): List<Tag>{
        val dataReadFuture: Future<List<Tag>> = AppDatabase.databaseWriterExecutor.submit(
            Callable {
                tag_Dao.listAllTagsFromName(name)
            })
        return try {
            while (!dataReadFuture.isDone) {
            }
            dataReadFuture.get()
        }catch(e: ExecutionException){
            emptyList()
        }
        catch(e: InterruptedException){
            emptyList()
        }
    }
    fun getTagsID(task_id: Int): List<Tag>{
        val dataReadFuture: Future<List<Tag>> = AppDatabase.databaseWriterExecutor.submit(
            Callable {
                tag_Dao.listAllTagsFromTask(task_id)
            })
        return try {
            while (!dataReadFuture.isDone) {
            }
            dataReadFuture.get()
        }catch(e: ExecutionException){
            emptyList()
        }
        catch(e: InterruptedException){
            emptyList()
        }
    }
    fun updateList(list: list){
        AppDatabase.databaseWriterExecutor.execute { list_dao.updateList(list) }
    }
    fun updateNotif(notification: Notification){
        AppDatabase.databaseWriterExecutor.execute { notif_Dao.changeNotification(notification) }
    }
    fun updateTask(task: Task){
        AppDatabase.databaseWriterExecutor.execute { task_Dao.changeTask(task) }
    }
    fun updateTag(tag: Tag){
        AppDatabase.databaseWriterExecutor.execute { tag_Dao.changeTag(tag) }
    }

    fun deleteList(list: list): Int{
        val dataReadFuture: Future<Int> = AppDatabase.databaseWriterExecutor.submit(
            Callable {
                list_dao.deleteList(list)
            })
        return try {
            while (!dataReadFuture.isDone) {
            }
            dataReadFuture.get()
        }catch(e: ExecutionException){
            -1
        }
        catch(e: InterruptedException){
            -1
        }
    }

    fun deleteTask(task: Task): Int{
        val dataReadFuture: Future<Int> = AppDatabase.databaseWriterExecutor.submit(
            Callable {
                task_Dao.deleteTask(task)
            })
        return try {
            while (!dataReadFuture.isDone) {
            }
            dataReadFuture.get()
        }catch(e: ExecutionException){
            -1
        }
        catch(e: InterruptedException){
            -1
        }
    }

    fun deleteNotif(notification: Notification): Int{
        val dataReadFuture: Future<Int> = AppDatabase.databaseWriterExecutor.submit(
            Callable {
                notif_Dao.deleteNotification(notification)
            })
        return try {
            while (!dataReadFuture.isDone) {
            }
            dataReadFuture.get()
        }catch(e: ExecutionException){
            -1
        }
        catch(e: InterruptedException){
            -1
        }
    }

    fun deleteTag(tag: Tag): Int{
        val dataReadFuture: Future<Int> = AppDatabase.databaseWriterExecutor.submit(
            Callable {
                tag_Dao.deleteTag(tag)
            })
        return try {
            while (!dataReadFuture.isDone) {
            }
            dataReadFuture.get()
        }catch(e: ExecutionException){
            -1
        }
        catch(e: InterruptedException){
            -1
        }
    }
}