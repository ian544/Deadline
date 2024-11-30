package ca.unb.mobiledev.deadlinesketch

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.widget.Toast
import ca.unb.mobiledev.deadlinesketch.entity.Task
import ca.unb.mobiledev.deadlinesketch.repo.dbRepo

class LoadTask(private val activity: AppCompatActivity) {
//    private val appContext: Context = activity.applicationContext
//    private var dbRepo: dbRepo = dbRepo(appContext)
    private var dbRepo:dbRepo = dbRepo(activity.applicationContext)



    private var recyclerView: RecyclerView? = null

    fun setRecyclerView(recyclerView: RecyclerView?): LoadTask {
        this.recyclerView = recyclerView
        return this
    }

    fun execute() {
        AppExecutors.databaseExecutor.execute{

            val tasksjson = dbRepo.getTaskAll()

            for (i in 1 until DOWNLOAD_TIME) {
                sleep()
            }
            AppExecutors.mainThreadExecutor.execute { updateDisplay(tasksjson)}
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

    private fun updateDisplay(taskList: List<Task>) {
        setupRecyclerView(taskList)
        //Toast.makeText(appContext, "File loaded", Toast.LENGTH_LONG).show()
    }

    private fun setupRecyclerView(taskList: List<Task>) {
        recyclerView?.adapter = TaskAdapter(activity, taskList)
        recyclerView?.addItemDecoration(DeadlineTaskBorder(activity))
    }

    companion object {
        private const val DOWNLOAD_TIME = 5 // Download time simulation
    }
}