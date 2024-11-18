package ca.unb.mobiledev.deadlinesketch

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TaskDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details_task)

        var intent = intent
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
                    val taskID = 1  //need ID from DB
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
    }
}