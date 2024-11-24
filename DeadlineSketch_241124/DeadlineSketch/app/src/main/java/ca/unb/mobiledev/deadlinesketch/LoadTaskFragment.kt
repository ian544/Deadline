package ca.unb.mobiledev.deadlinesketch

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import ca.unb.mobiledev.deadlinesketch.entity.Task
import ca.unb.mobiledev.deadlinesketch.repo.dbRepo

class LoadTaskFrag(private val activity: FragmentActivity, private val listName: String) {
    private val appContext: Context = activity.applicationContext
    private var recyclerView: RecyclerView? = null
    private var dbRepo: dbRepo = dbRepo(appContext)

    fun setRecyclerView(recyclerView: RecyclerView?): LoadTaskFrag {
        this.recyclerView = recyclerView
        return this
    }

    fun execute() {
        AppExecutors.databaseExecutor.execute{
            var dbList = dbRepo.getSingleListName(listName)
            var dbTasks = dbRepo.getTaskList(dbList[0].list_id)

            for (i in 1 until DOWNLOAD_TIME) {
                sleep()
            }
            AppExecutors.mainThreadExecutor.execute { updateDisplay(dbTasks)}
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