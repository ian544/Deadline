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
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ca.unb.mobiledev.deadlinesketch.Repo.dbRepo
import kotlinx.coroutines.delay
import java.lang.Thread.sleep

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val dbRepo: dbRepo = dbRepo(this)
        var listSize = dbRepo.getList().size
        if(listSize == 0){
            dbRepo.insertList("To_Do")
            dbRepo.insertList("Archive")
            dbRepo.insertList("Planning")
            sleep(2500) //if you don't sleep, and the next if statement triggers, errors occur
        }
        var taskSize = dbRepo.getTaskAll().size
        if(taskSize == 0){
            dbRepo.insertTask(dbRepo.getList()[0].list_id, "Test Task", "This task is a test task", "2024-12-02", "2024-09-04","Emergancy")
            sleep(2500)
        }
        //val navController = findNavController(R.id.fragmentContainerView)

        //val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        //val navController = navHostFragment.navController

        val recyclerView: RecyclerView = findViewById(R.id.deadlineList)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val dataTask = LoadTask(this)
        dataTask.setRecyclerView(recyclerView)
        dataTask.execute()


        val gearIcon: ImageView = findViewById(R.id.gearIcon)
        gearIcon.setOnClickListener {
            val settingsDialog = SettingsDialogFragment()
            settingsDialog.show(supportFragmentManager, "SettingsDialog")

//            view ->
//            val inflater: LayoutInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
//            val popupView: View = inflater.inflate(R.layout.popup_layout, null)
//            val popupWindow = PopupWindow(
//                popupView,
//                ConstraintLayout.LayoutParams.WRAP_CONTENT,
//                ConstraintLayout.LayoutParams.WRAP_CONTENT
//            )
//
//            popupWindow.setBackgroundDrawable(GradientDrawable().apply {
//                setColor(Color.WHITE)
//                setStroke(2, Color.BLACK)
//            })
//            popupWindow.isFocusable = true
//            popupWindow.showAsDropDown(view, 0, 0)
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