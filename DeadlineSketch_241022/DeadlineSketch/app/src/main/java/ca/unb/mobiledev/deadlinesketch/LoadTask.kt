package ca.unb.mobiledev.deadlinesketch

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import java.util.concurrent.Executors

class LoadTask(private val activity: AppCompatActivity) {
    private val appContext: Context = activity.applicationContext
    private var recyclerView: RecyclerView? = null

    fun setRecyclerView(recyclerView: RecyclerView?): LoadTask {
        this.recyclerView = recyclerView
        return this
    }

    fun execute() {
        Executors.newSingleThreadExecutor()
            .execute {
                val mainHandler = Handler(Looper.getMainLooper())
                val jsontasks = JsonUtils(appContext)
                val tasksjson = jsontasks.getTasks()

                for (i in 1 until DOWNLOAD_TIME) {
                    sleep()
                }
                mainHandler.post { updateDisplay(tasksjson) }
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

    private fun updateDisplay(taskList: ArrayList<Task>) {
        setupRecyclerView(taskList)
        Toast.makeText(appContext, "File loaded", Toast.LENGTH_LONG).show()
    }

    private fun setupRecyclerView(taskList: ArrayList<Task>) {
        recyclerView?.adapter = TaskAdapter(activity, taskList)
        recyclerView?.addItemDecoration(DeadlineTaskBorder(activity))
    }

    companion object {
        private const val DOWNLOAD_TIME = 5 // Download time simulation
    }
}