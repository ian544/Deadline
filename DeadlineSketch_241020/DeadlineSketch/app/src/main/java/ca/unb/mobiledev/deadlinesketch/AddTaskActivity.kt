package ca.unb.mobiledev.deadlinesketch

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AddTaskActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        val taskreturn: Button = findViewById(R.id.submit_button)
        taskreturn.setOnClickListener{
            submitTask()
        }

    }

    fun submitTask() {
        val title = findViewById<EditText>(R.id.task_title).text.toString()
        val description = findViewById<EditText>(R.id.task_description).text.toString()

        // For now, just log the inputs
        Log.d("AddTaskActivity", "Title: $title, Description: $description")

        finish()
    }
}