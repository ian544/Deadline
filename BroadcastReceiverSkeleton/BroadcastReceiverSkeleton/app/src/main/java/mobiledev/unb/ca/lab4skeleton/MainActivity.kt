package mobiledev.unb.ca.lab4skeleton

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.*
import android.content.ContentValues.TAG
import android.content.Intent.ACTION_BATTERY_LOW
import android.content.Intent.ACTION_BATTERY_OKAY
import android.content.Intent.ACTION_POWER_CONNECTED
import android.content.Intent.ACTION_POWER_DISCONNECTED
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.ZoneId
import java.util.*

class MainActivity : AppCompatActivity() {
    // Attributes for working with an alarm
    private var alrmMngr: AlarmManager? = null
    private lateinit var alrmRecInt: PendingIntent

    private var isReacuring: Boolean = false;
    private val intFilt = IntentFilter()

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkNotificationPermissions()
        alrmRecInt = Intent(this, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        }
        alrmMngr = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val enterBtn = findViewById<Button>(R.id.setNotif)
        val dateField = findViewById<EditText>(R.id.editTextDate)
        val timeField = findViewById<EditText>(R.id.timeStart)
        val recTimeField = findViewById<EditText>(R.id.timeRecurring)
        val isReacurringButton = findViewById<ToggleButton>(R.id.isReac)
        val existNotificationField = findViewById<TextView>(R.id.existingAlarms)
        val clearBtn = findViewById<Button>(R.id.button2)
        isReacurringButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                isReacuring = true
            } else {
                isReacuring = false
            }
        }

        clearBtn.setOnClickListener{
            alrmMngr!!.cancel(alrmRecInt)
        }
        enterBtn.setOnClickListener {
            val curDate =
                LocalDate.ofInstant(Calendar.getInstance().toInstant(), ZoneId.systemDefault())
            val askedTimeDate = LocalDate.parse(dateField.text.toString())
            val period = Period.between(curDate, askedTimeDate)
            val curTime =
                LocalDateTime.ofInstant(Calendar.getInstance().toInstant(), ZoneId.systemDefault())

            var numOfDays = period.days

            Log.i(ContentValues.TAG, numOfDays.toString())
            var timeArray: List<String> = timeField.text.toString().split(":")
            var hours = timeArray[0].toInt() - curTime.hour
            var minutes = timeArray[1].toInt() - curTime.minute
            Log.i(TAG, "$hours $minutes")

            if (isReacuring) {
                var recTimeArray: List<String> = recTimeField.text.toString().split(":")
                var recHours = recTimeArray[0].toInt()
                var recMinutes = recTimeArray[1].toInt()
                alrmMngr?.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + (numOfDays * 24 * 60 * 60 * 1000 + hours * 60 * 60 * 1000 + minutes * 60 * 1000).toLong(),
                    (1000 * 60 * recMinutes + 1000 * 60 * 60 * recHours).toLong(),
                    alrmRecInt
                )
            } else {
                Log.i(
                    TAG,
                    (numOfDays * 24 * 60 * 60 * 1000 + hours * 60 * 60 * 1000 + minutes * 60 * 1000).toString()
                )

                if (alrmMngr!!.canScheduleExactAlarms()) {
                    alrmMngr?.setExact(
                        AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis() + (numOfDays * 24 * 60 * 60 * 1000 + hours * 60 * 60 * 1000 + minutes * 60 * 1000).toLong(),
                        alrmRecInt
                    )
                } else {
                    alrmMngr?.set(
                        AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis() + (numOfDays * 24 * 60 * 60 * 1000 + hours * 60 * 60 * 1000 + minutes * 60 * 1000).toLong(),
                        alrmRecInt
                    )
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        // TODO: Unregister the battery receivers to avoid memory leaks
    }

    /**
     * Checks for the appropriate permissions
     */
    private fun checkNotificationPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestRuntimePermissions()
            }
        }
    }

    /**
     * Grants the appropriate permissions
     */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestRuntimePermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.POST_NOTIFICATIONS
            ), Constants.NOTIFICATION_REQUEST_ID
        )
    }

}