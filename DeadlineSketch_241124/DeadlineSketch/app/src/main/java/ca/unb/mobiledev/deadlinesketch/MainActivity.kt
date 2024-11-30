package ca.unb.mobiledev.deadlinesketch

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
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
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ca.unb.mobiledev.deadlinesketch.repo.dbRepo
import java.lang.Thread.sleep

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var dataTask: LoadTask
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        var dbRepo: dbRepo = dbRepo(this)
//        Log.i(TAG,"Testing entry before dbRepo call")
//        dbRepo = dbRepo(applicationContext)
//        Log.i(TAG, "Testing after attempted initialization")
        //dbRepo = dbRepo.getInstance(applicationContext)

        var listSize = dbRepo.getList().size
        if(listSize == 0){
            dbRepo.insertList("To_Do")
            sleep(1000)
            dbRepo.insertList("Archive")
            sleep(1000)
            dbRepo.insertList("Planning")
            sleep(2500) //if you don't sleep, and the next if statement triggers, errors occur
        }
        var taskSize = dbRepo.getTaskAll().size
        if(taskSize == 0){
            dbRepo.insertTask(dbRepo.getList()[0].list_id, "Test Task", "This task is a test task", "2024-12-02", "2024-09-04","Emergency")
            sleep(2500)
        }

        recyclerView = findViewById(R.id.deadlineList)
        recyclerView.layoutManager = LinearLayoutManager(this)

        dataTask = LoadTask(this)
        dataTask.setRecyclerView(recyclerView)
        dataTask.execute()

        val gearIcon: ImageView = findViewById(R.id.gearIcon)
        gearIcon.setOnClickListener {
            val settingsDialog = SettingsDialogFragment()
            settingsDialog.show(supportFragmentManager, "SettingsDialog")

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

    override fun onResume() {
        super.onResume()
        //Note: less than ideal way to reload lists; temporary measure only
        dataTask = LoadTask(this)
        dataTask.setRecyclerView(recyclerView)
        dataTask.execute()
    }
}