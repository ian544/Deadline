package ca.unb.mobiledev.deadlinesketch

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.widget.Toast
import ca.unb.mobiledev.deadlinesketch.Repo.dbRepo
import ca.unb.mobiledev.deadlinesketch.entity.Task

class LoadTask(private val activity: AppCompatActivity) {
    private val appContext: Context = activity.applicationContext
    private var recyclerView: RecyclerView? = null
    private var dbRepo: dbRepo = dbRepo(appContext)

    fun setRecyclerView(recyclerView: RecyclerView?): LoadTask {
        this.recyclerView = recyclerView
        return this
    }

    fun execute() {
        AppExecutors.databaseExecutor.execute{
            val tasks = dbRepo.getTaskAll()
            val jsontasks = JsonUtils(appContext)
            val tasksjson = jsontasks.getTasks()


            for (i in 1 until DOWNLOAD_TIME) {
                sleep()
            }
            AppExecutors.mainThreadExecutor.execute { updateDisplay(tasks)}
        }
    }

    private fun sleep() {
        try {
            val mDelay = 500
            Thread.sleep(mDelay.toLong())
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun updateDisplay(taskList: List<ca.unb.mobiledev.deadlinesketch.entity.Task>) {
        setupRecyclerView(taskList)
        Toast.makeText(appContext, "File loaded", Toast.LENGTH_LONG).show()
    }

    private fun setupRecyclerView(taskList: List<Task>) {
        recyclerView?.adapter = TaskAdapter(activity, taskList)
        recyclerView?.addItemDecoration(DeadlineTaskBorder(activity))
    }

    companion object {
        private const val DOWNLOAD_TIME = 5 // Download time simulation
    }
}