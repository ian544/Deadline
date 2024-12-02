package ca.unb.mobiledev.deadlinesketch

import android.app.AlertDialog
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.Button
import android.widget.TextView
import ca.unb.mobiledev.deadlinesketch.repo.dbRepo

class DetailActivity : AppCompatActivity() {
    private lateinit var dbRepo: dbRepo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        dbRepo = dbRepo(applicationContext)

        var intent = intent
        var taskTitle = intent.getStringExtra("taskTitle")
        var taskDescription = intent.getStringExtra("taskDescription")
        var taskDueDate = intent.getStringExtra("taskDueDate")
        var taskID = intent.getIntExtra("taskID",-1)

        val descView = findViewById<TextView>(R.id.description_textview)
        descView.text = taskDescription
        descView.movementMethod = ScrollingMovementMethod()

        val dueDateView = findViewById<TextView>(R.id.dueDate_textview)
        dueDateView.text = getString(R.string.due_date_prefix, taskDueDate)

        val titleView = findViewById<TextView>(R.id.taskTitle_textview)
        titleView.text = taskTitle

        val exitTaskButton: Button = findViewById(R.id.HomePageButton)
        exitTaskButton.setOnClickListener {
            finish()
        }
        val completeTask: Button = findViewById(R.id.complete_task_button)
        completeTask.setOnClickListener {
            Log.i(TAG, "nothing happening")
            AlertDialog.Builder(this)
                .setTitle("Complete Task")
                .setMessage("Update task status and move to Archive?")
                .setPositiveButton("Yes"){_,_ ->
                    val taskToComplete = dbRepo.getTaskSingle(taskID)
                    taskToComplete[0].status = "Complete"
                    val changeToArchive = dbRepo.getSingleListName("Archive")
                    taskToComplete[0].list_id = changeToArchive[0].list_id
                    dbRepo.defaultUpdateTask(taskToComplete[0])
                    val notifToUpdate = dbRepo.getNotif(taskID)
                    notifToUpdate[0].disabled = true
                    dbRepo.defaultUpdateNotif(notifToUpdate[0])
                    finish()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
}