package ca.unb.mobiledev.deadlinesketch

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class TestActivity : AppCompatActivity() {

    private lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        spinner = findViewById(R.id.spinner_view_selection)

        // Set up Spinner
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.calendar_views,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // Handle Spinner Selection
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> loadFragment(WeekViewFragment())
                    1 -> loadFragment(MonthViewFragment())
                    2 -> loadFragment(DayViewFragment())
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Load default view (Week View)
        loadFragment(WeekViewFragment())
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.calendar_container, fragment)
            .commit()
    }
}