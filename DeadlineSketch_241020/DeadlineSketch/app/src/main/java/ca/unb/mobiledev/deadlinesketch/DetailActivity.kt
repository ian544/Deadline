package ca.unb.mobiledev.deadlinesketch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.TextView

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        var intent = intent
        val taskTitle = intent.getStringExtra("taskTitle")
        val taskDescription = intent.getStringExtra("taskDescription")
        val taskDueDate = intent.getStringExtra("taskDueDate")

        val descView = findViewById<TextView>(R.id.description_textview)
        descView.text = taskDescription
        descView.movementMethod = ScrollingMovementMethod()

        val dueDateView = findViewById<TextView>(R.id.dueDate_textview)
        dueDateView.text = taskDueDate

        this.title = taskTitle
    }
}