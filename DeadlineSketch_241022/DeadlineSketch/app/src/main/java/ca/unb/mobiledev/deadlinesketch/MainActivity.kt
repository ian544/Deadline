package ca.unb.mobiledev.deadlinesketch

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.PopupWindow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.deadlineList)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val dataTask = LoadTask(this)
        dataTask.setRecyclerView(recyclerView)
        dataTask.execute()


        val gearIcon: ImageView = findViewById(R.id.gearIcon)
        gearIcon.setOnClickListener { view ->
            val inflater: LayoutInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val popupView: View = inflater.inflate(R.layout.popup_layout, null)
            val popupWindow = PopupWindow(
                popupView,
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )

            popupWindow.setBackgroundDrawable(GradientDrawable().apply {
                setColor(Color.WHITE)
                setStroke(2, Color.BLACK)
            })
            popupWindow.isFocusable = true
            popupWindow.showAsDropDown(view, 0, 0)
        }
        val testButton: Button = findViewById(R.id.testButton)
        testButton.setOnClickListener {
            val intent = Intent(this, TestActivity::class.java)
            startActivity(intent)
        }
        val todoButton: Button = findViewById(R.id.ToDoButton)
        todoButton.setOnClickListener {
            val intent = Intent(this, ToDoActivity::class.java)
            startActivity(intent)
        }
    }
}