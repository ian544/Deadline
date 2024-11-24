package ca.unb.mobiledev.deadlinesketch.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ca.unb.mobiledev.deadlinesketch.dao.NotificationDao
import ca.unb.mobiledev.deadlinesketch.dao.TagDao
import ca.unb.mobiledev.deadlinesketch.dao.TaskDao
import ca.unb.mobiledev.deadlinesketch.dao.listDao
import ca.unb.mobiledev.deadlinesketch.entity.list
import ca.unb.mobiledev.deadlinesketch.entity.Task
import ca.unb.mobiledev.deadlinesketch.entity.Tag
import ca.unb.mobiledev.deadlinesketch.entity.Notification

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Database(entities = [list::class, Task::class, Tag::class, Notification::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun listDao(): listDao
    abstract fun notificationDao(): NotificationDao
    abstract fun taskDao(): TaskDao
    abstract fun tagDao(): TagDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase ?= null
        private const val NUM_THREADS = 4
        val databaseWriterExecutor: ExecutorService = Executors.newFixedThreadPool(NUM_THREADS)


        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "Deadline_DB").build()
                INSTANCE = instance
                return instance
            }
        }
    }
}