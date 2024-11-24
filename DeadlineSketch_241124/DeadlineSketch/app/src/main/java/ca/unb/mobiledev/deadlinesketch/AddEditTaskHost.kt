package ca.unb.mobiledev.deadlinesketch

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2

class AddEditTaskHost : AppCompatActivity() {
    private lateinit var viewModel: TaskViewModel
    lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.host_add_edit_task)
        //val dbRepo: dbRepo = dbRepo(this)

        viewModel = ViewModelProvider(this)[TaskViewModel::class.java]

        viewPager = findViewById(R.id.viewPager_2)
        viewPager.adapter = AddTaskPagerAdapter(this)


        if ((viewPager.adapter?.itemCount ?: 0) <= 1) {
            Log.i("AddEditTaskHost", "Method failed: viewPager size is not greater than 1")
        } //may be able to remove since task addition fragments can't be deleted

        viewPager.setCurrentItem(0,false)

        //Determine Edit Mode
        val isEditMode = intent.getBooleanExtra("isEditMode",false)
        if(isEditMode){
            //EditMode active
            val taskID = intent.getIntExtra("taskID", 0)
            //val returnedTask = dbRepo.getTaskSingle(taskID)
            //TODO: Fetch task data from DB with task ID; pass to viewModel
            updateViewModelFromDB(taskID)
        }
    }

    private fun updateViewModelFromDB(taskID: Int){
        var title = "Example Title" //Replace with DB data
        var dueDate = "Example DueDate" //Replace with DB data; may need long values
        var description = "Example Description" //Replace with DB data
        var setTag = "Example Tag"  //Replace with DB data
        var setList = "Example list"    //Replace with DB data
        var setActivationDate = "Example Date"  //Replace with DB data; may need long values
        var setpriority = "Example priority"    //Replace with DB data
        var notifTitle = "Example notif title"  //Replace with DB data
        var notifDesc = "Example notif description" //Replace with DB data
        var notifDate = "Example notif active date" //Replace with DB data; may need long values
        var notifTime = "Example notif active time" //Replace with DB data; may need long values
        var notifConfirmRepeating: Boolean = false  //Replace with DB data
        var notifinterval = "Example notif interval"    //Replace with DB data

        viewModel.title = title
        viewModel.dueDate = dueDate
        viewModel.description = description
        viewModel.setTag = setTag
        viewModel.setList = setList
        viewModel.setActivationDate = setActivationDate
        viewModel.setpriority = setpriority
        viewModel.notifTitle = notifTitle
        viewModel.notifDesc = notifDesc
        viewModel.notifDate = notifDate
        viewModel.notifTime = notifTime
        viewModel.notifConfirmRepeating = notifConfirmRepeating
        viewModel.notifinterval = notifinterval
    }
}
