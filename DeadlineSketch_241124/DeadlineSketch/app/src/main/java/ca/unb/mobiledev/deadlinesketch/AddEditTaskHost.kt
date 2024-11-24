package ca.unb.mobiledev.deadlinesketch

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import ca.unb.mobiledev.deadlinesketch.entity.Notification
import ca.unb.mobiledev.deadlinesketch.entity.Tag
import ca.unb.mobiledev.deadlinesketch.entity.Task
import ca.unb.mobiledev.deadlinesketch.entity.list
import ca.unb.mobiledev.deadlinesketch.repo.dbRepo

class AddEditTaskHost : AppCompatActivity() {
    private lateinit var viewModel: TaskViewModel
    lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.host_add_edit_task)
        val dbRepo: dbRepo = dbRepo(this)

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
            val returnedTask = dbRepo.getTaskSingle(taskID)
            val returnedTag = dbRepo.getTagsID(taskID)
            val returnedNotif = dbRepo.getNotif(taskID)
            val returnedList = dbRepo.getSingleList(returnedTask[0].list_id)
            //TODO: Fetch task data from DB with task ID; pass to viewModel
            updateViewModelFromDB(returnedTask, returnedTag, returnedNotif, returnedList)
        }
    }

    private fun updateViewModelFromDB(
        taskList: List<Task>,
        returnedTag: List<Tag>,
        returnedNotif: List<Notification>,
        returnedList: List<list>
    ){
        val task = taskList[0]



        var title = task.title //Replace with DB data
        var dueDate = task.due_date //Replace with DB data; may need long values
        var description = task.description //Replace with DB data
        var setTag = "" //Replace with DB data
        var setList = ""    //Replace with DB data
        var setActivationDate = task.activate_time  //Replace with DB data; may need long values
        var setpriority = task.priority    //Replace with DB data
        var notifTitle = "Example notif title"  //Replace with DB data
        var notifDesc = "Example notif description" //Replace with DB data
        var notifDate = "Example notif active date" //Replace with DB data; may need long values
        var notifTime = "Example notif active time" //Replace with DB data; may need long values
        var notifConfirmRepeating: Boolean = false  //Replace with DB data
        var notifinterval = "Example notif interval"    //Replace with DB data

        setList = returnedList[0].list_name
        if(returnedTag.size > 0){
            val tag = returnedTag[0]
            setTag = tag.tag_name
        }



        if(returnedNotif.size > 0){
            val notif = returnedNotif[0]
            notifTitle = notif.notification_name
            notifTime = notif.activation_time
            notifConfirmRepeating = notif.isReacurring
            notifDate = notif.activation_date
            notifDesc = notif.notification_description
            notifinterval = notif.reaccuring_time
        }

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
