package ca.unb.mobiledev.deadlinesketch

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ca.unb.mobiledev.deadlinesketch.repo.dbRepo

class TaskDetails : AppCompatActivity() {
    private lateinit var dbRepo: dbRepo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details_task)
        dbRepo = dbRepo(applicationContext)


        var intent = intent
        var taskID = intent.getIntExtra("taskID",-1)
        Log.i(TAG,"Task Details: " + taskID.toString())
        var taskTitle = intent.getStringExtra("taskTitle")
        var taskDescription = intent.getStringExtra("taskDescription")
        var taskDueDate = intent.getStringExtra("taskDueDate")
        var taskPriority = intent.getStringExtra("taskPriority")

        val descView = findViewById<TextView>(R.id.description_textview)
        descView.text = taskDescription
        descView.movementMethod = ScrollingMovementMethod()

        val priorityView = findViewById<TextView>(R.id.priority_textview)
        priorityView.text = taskPriority

        val dueDateView = findViewById<TextView>(R.id.dueDate_textview)
        dueDateView.text = getString(R.string.due_date_prefix, taskDueDate)

        val titleView = findViewById<TextView>(R.id.taskTitle_textview)
        titleView.text = taskTitle

        val exitTaskButton: Button = findViewById(R.id.toListButton3)
        exitTaskButton.setOnClickListener {
            finish()
        }

        val editTaskButton: Button = findViewById(R.id.EditTaskButton)
        editTaskButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Edit Task")
                .setMessage("Are you sure you want to edit this task?")
                .setPositiveButton("Yes") { _, _ ->
                    val editTask = Intent(this, AddEditTaskHost::class.java)
                    editTask.putExtra("isEditMode", true)
                    editTask.putExtra("taskID", taskID)
                    startActivity(editTask)
                    //Remove TaskDetails activity from backstack
                    finish()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        val deleteTask: Button = findViewById(R.id.delete_task_button)
        deleteTask.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("!!!Warning - Delete Task - Warning!!!")
                .setMessage("Action will permanently delete this task!\n\nAre you sure you want to continue?")
                .setPositiveButton("Yes") { _, _ ->
                    val taskToDelete = dbRepo.getTaskSingle(taskID)
                    val notifToDelete = dbRepo.getNotif(taskID)
                    val tagToDelete = dbRepo.getTagsID(taskID)
                    if(tagToDelete.isNotEmpty()){
                        dbRepo.deleteTag(tagToDelete[0])
                    }else{
                        Log.i(TAG, "Tag record not found.")
                    }
                    if(notifToDelete.isNotEmpty()){
                        dbRepo.deleteNotif(notifToDelete[0])
                    }else{
                        Log.i(TAG, "Notification record not found.")
                    }
                    dbRepo.deleteTask(taskToDelete[0])
                    //Remove TaskDetails activity from backstack
                    finish()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
}