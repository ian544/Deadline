package ca.unb.mobiledev.deadlinesketch

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import ca.unb.mobiledev.deadlinesketch.repo.dbRepo

class ToDoActivity : AppCompatActivity() {
    private lateinit var dbRepo: dbRepo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_to_do)

        dbRepo = dbRepo(applicationContext)
        var listList = dbRepo.getList()
        var i = 0
        var listNames: MutableList<String> = mutableListOf<String>()
        while(i < listList.size){
            listNames.add(i, listList[i].list_name)
            i = i+1
        }
        val fragmentNames = listNames
        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        viewPager.adapter = ListPagerAdapter(this, fragmentNames)


        if ((viewPager.adapter?.itemCount ?: 0) <= 1) {
            Log.i("ToDoActivity", "Method failed: viewPager size is not greater than 1")
        }

        viewPager.setCurrentItem(1, false)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@ToDoActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }
        this.onBackPressedDispatcher.addCallback(this, callback)
    }
}