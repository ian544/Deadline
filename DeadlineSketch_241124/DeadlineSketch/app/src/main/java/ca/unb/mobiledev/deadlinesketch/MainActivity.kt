package ca.unb.mobiledev.deadlinesketch

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ca.unb.mobiledev.deadlinesketch.repo.dbRepo
import java.lang.Thread.sleep

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var dataTask: LoadTask
    private var alrmMngr: AlarmManager? = null
    private lateinit var alrmRecInt: PendingIntent
    private var isReacuring: Boolean = false;
    private val intFilt = IntentFilter()

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NotifUtils.checkNotificationPermissions(this)
        alrmRecInt = Intent(this, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        }
        alrmMngr = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        enableEdgeToEdge()

        setContentView(R.layout.activity_main)
        var dbRepo: dbRepo = dbRepo(this)

        var listSize = dbRepo.getList().size
        if(listSize == 0){
            dbRepo.insertList("To_Do")
            sleep(100)
            dbRepo.insertList("Archive")
            sleep(100)
            dbRepo.insertList("Planning")
            sleep(200) //if you don't sleep, and the next if statement triggers, errors occur
        }

        val unscheduledNotifs = dbRepo.getAllUnscheduledNotif()
        //TODO Add function for rescheduling notifications
        if(unscheduledNotifs.isEmpty())
            Log.i(TAG, "No unscheduled notifications.")
        unscheduledNotifs.forEach { notif ->
            val intent = Intent(this, AlarmReceiver::class.java).apply {
                putExtra("notifID", notif.notification.notification_id)
                putExtra("title", notif.notification.notification_name)
                putExtra("message", notif.notification.notification_description)
                putExtra("priority", notif.priority)
                //Adding task priority wouldn't be a bad next step if possible
            }
            val pendingIntent = PendingIntent.getBroadcast(this,notif.notification.notification_id,intent,PendingIntent.FLAG_UPDATE_CURRENT)
            val triggerTime = NotifUtils.calculateTriggerTime(notif.notification)
            NotifUtils.scheduleAlarm(alrmMngr!!,pendingIntent,notif,triggerTime)
            //Note: does not have null pointer exception protection

            notif.notification.scheduled = true
            dbRepo.defaultUpdateNotif(notif.notification)
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
            val intent = Intent(this, CalendarActivity::class.java)
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

    override fun onDestroy() {
        super.onDestroy()
        // TODO: Unregister the battery receivers to avoid memory leaks
    }
}