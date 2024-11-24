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

class ToDoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_to_do)

        val fragmentNames = listOf("ToDo List", "Archive List", "Planning List")
        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        viewPager.adapter = ListPagerAdapter(this, fragmentNames)


        if ((viewPager.adapter?.itemCount ?: 0) <= 1) {
            Log.i("ToDoActivity", "Method failed: viewPager size is not greater than 1")
        }

        viewPager.setCurrentItem(1, false)
        //val navController = findNavController(R.id.fragmentContainerView)

        //val prevButton: Button = findViewById(R.id.prevButton)
        //val nextButton: Button = findViewById(R.id.nextButton)

//        prevButton.setOnClickListener {
//            val previousItem = (viewPager.currentItem - 1 + fragmentNames.size) % fragmentNames.size
//            viewPager.currentItem = previousItem
//        }
//
//        nextButton.setOnClickListener {
//            val nextItem = (viewPager.currentItem + 1) % fragmentNames.size
//            viewPager.currentItem = nextItem
//        }

        //Schedule for removal if home back press is sufficient (testing needed)
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