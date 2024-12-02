package ca.unb.mobiledev.deadlinesketch

import android.content.ContentValues.TAG
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
    private lateinit var dbRepo: dbRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.host_add_edit_task)
        dbRepo = dbRepo(applicationContext)

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
            Log.i(TAG, taskID.toString())
            val returnedTask = dbRepo.getTaskSingle(taskID)
            val returnedTag = dbRepo.getTagsID(taskID)
            val returnedNotifSize = dbRepo.getNotifAll()
            val returnedNotif = dbRepo.getNotif(taskID)
            Log.i(TAG,"SIZE OF ALL NOTIFS " + returnedNotifSize.size.toString())
            val returnedList = dbRepo.getSingleList(returnedTask[0].list_id)
            updateViewModelFromDB(returnedTask, returnedTag, returnedNotif, returnedList)
            viewModel.isEditMode = true
        }
    }

    private fun updateViewModelFromDB(
        taskList: List<Task>,
        returnedTag: List<Tag>,
        returnedNotif: List<Notification>,
        returnedList: List<list>
    ){
        val task = taskList[0]

        var title = task.title
        var status = task.status
        Log.i(TAG, "task status: "+status)
        var notifDisabled = false
        var dueDate = task.due_date
        var description = task.description
        var tagID = 0
        var setTag = ""
        var setList = returnedList[0].list_name
        var originalList = returnedList[0].list_id
        var originalName = returnedList[0].list_name
        var setActivationDate = task.activate_time
        var setpriority = task.priority
        var notifID = 0
        var notifTitle = ""
        var notifDesc = ""
        var notifDate = ""
        var notifTime = ""
        var notifConfirmRepeating = false
        var notifinterval = ""

        if(returnedTag.isNotEmpty()){
            val tag = returnedTag[0]
            setTag = tag.tag_name
            tagID = tag.tag_id
        }

        if(returnedNotif.isNotEmpty()){
            val notif = returnedNotif[0]
            notifID = notif.notification_id
            notifTitle = notif.notification_name
            notifTime = notif.activation_time
            notifConfirmRepeating = notif.isReacurring
            notifDate = notif.activation_date
            notifDesc = notif.notification_description
            notifinterval = notif.reaccuring_time
            notifDisabled = notif.disabled
        }

        viewModel.notifDisabled = notifDisabled
        viewModel.title = title
        viewModel.dueDate = dueDate
        viewModel.description = description
        viewModel.status = status
        viewModel.tagID = tagID
        viewModel.setTag = setTag
        viewModel.setList = setList
        viewModel.originalList = originalList
        viewModel.originalListName = originalName
        viewModel.setActivationDate = setActivationDate
        viewModel.setpriority = setpriority
        viewModel.notifID = notifID
        viewModel.notifTitle = notifTitle
        viewModel.notifDesc = notifDesc
        viewModel.notifDate = notifDate
        viewModel.notifTime = notifTime
        viewModel.notifConfirmRepeating = notifConfirmRepeating
        viewModel.notifinterval = notifinterval
        viewModel.taskID = task.task_id
        Log.i(TAG, "ViewModel task_id: "+ viewModel.taskID)
    }
}
