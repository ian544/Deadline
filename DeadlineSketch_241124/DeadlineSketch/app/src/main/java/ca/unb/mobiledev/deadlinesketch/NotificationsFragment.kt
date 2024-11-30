package ca.unb.mobiledev.deadlinesketch

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import ca.unb.mobiledev.deadlinesketch.entity.Notification
import ca.unb.mobiledev.deadlinesketch.entity.Tag
import ca.unb.mobiledev.deadlinesketch.entity.Task
import ca.unb.mobiledev.deadlinesketch.repo.dbRepo
import java.lang.Thread.sleep


class NotificationsFragment : Fragment() {
    private val viewModel: TaskViewModel by activityViewModels()
    private lateinit var viewPager: ViewPager2
    private lateinit var notifTitle: EditText
    private lateinit var notifDesc: EditText
    private lateinit var notifDate: EditText
    private lateinit var notifTime: Spinner
    private lateinit var notifConfirmRepeating: CheckBox
    private lateinit var notifinterval: Spinner
    private var positionInterval: Int = -1
    private var timePosition: Int = -1
    private lateinit var dbRepo: dbRepo

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dbRepo = dbRepo(context)
        viewPager = (context as AddEditTaskHost).viewPager
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val logo: ImageView = view.findViewById(R.id.logo_home)
        logo.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Warning!!")
                .setMessage("Unsaved data will be lost. Continue?")
                .setPositiveButton("Yes") { _, _ ->
                    val intent = Intent(activity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        notifTitle = view.findViewById(R.id.notificationTitle)
        notifDesc = view.findViewById(R.id.notificationdescription)
        notifDate = view.findViewById(R.id.notificationSetDate)
        notifTime = view.findViewById(R.id.time_spinner)
        notifConfirmRepeating = view.findViewById(R.id.notificationcheckBox)
        notifinterval = view.findViewById(R.id.list_spinner)

        val timeList = arrayOf("6:00", "6:30", "7:00", "7:30", "8:00", "8:30", "9:00", "9:30", "10:00",
            "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30",
            "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "20:00", "20:30", "21:00", "21:30", "22:00")
        val timeAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, timeList)
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        notifTime.adapter = timeAdapter

        val intervals = arrayOf("1 Day", "2 Days", "3 Days", "4 Days", "5 Days","6 Days","7 Days")
        val intervalAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, intervals)
        intervalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        notifinterval.adapter = intervalAdapter

        if(!viewModel.notifTitle.isNullOrEmpty()){
            notifTitle.setText(viewModel.notifTitle)
        }else{
            notifTitle.text = null
        }
        notifTitle.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.notifTitle = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        if(!viewModel.notifDesc.isNullOrEmpty()){
            notifDesc.setText(viewModel.notifDesc)
        }else{
            notifDesc.text = null
        }
        notifDesc.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.notifDesc = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        if(!viewModel.notifDate.isNullOrEmpty()){
            notifDate.setText(viewModel.notifDate)
        }else{
            notifDate.text = null
        }

        if(!viewModel.notifTime.isNullOrEmpty()){
            timePosition = timeAdapter.getPosition(viewModel.notifTime)
            if(timePosition >= 0){
                notifTime.setSelection(timePosition)
            }
        }else{
            Log.i(TAG, "Invalid time selection.")
        }

        notifConfirmRepeating.isChecked = viewModel.notifConfirmRepeating

        if(!viewModel.notifinterval.isNullOrEmpty()){
            positionInterval = intervalAdapter.getPosition(viewModel.notifinterval)
            if(positionInterval >= 0){
                notifinterval.setSelection(positionInterval)
            }else{
                Log.i(TAG, "Interval not present in selection list.")
            }
        }

        notifDate.setOnClickListener { showDatePickerDialog() }

        notifTime.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedTime = parent.getItemAtPosition(position).toString()
                viewModel.notifTime = selectedTime
                Log.i(TAG,selectedTime)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        notifinterval.isEnabled = notifConfirmRepeating.isChecked

        notifConfirmRepeating.setOnCheckedChangeListener { _, isChecked ->
            notifinterval.isEnabled = isChecked
            viewModel.notifConfirmRepeating = isChecked
        }

        notifinterval.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedInterval = parent.getItemAtPosition(position).toString()
                viewModel.notifinterval = selectedInterval
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        val submitTask: Button = view.findViewById(R.id.submit_task_button)
        submitTask.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setMessage("Submit Task to DataBase and Exit?")
                .setPositiveButton("Yes") { _, _ ->
                    val listID = dbRepo.getSingleListName(viewModel.setList)[0].list_id
                    Log.i(TAG, listID.toString())
                    var taskID = 0
                    var notifID = 0
                    var tagID = 0
                    var originalList = 0

                    val taskObject = Task()
                        //taskID
                        taskObject.list_id = listID
                        taskObject.title = viewModel.title
                        taskObject.due_date = viewModel.dueDate
                        taskObject.priority = viewModel.setpriority
                        taskObject.activate_time = viewModel.setActivationDate
                        taskObject.description = viewModel.description

                    val notifObject = Notification()


                        notifObject.notification_name = viewModel.notifTitle
                        notifObject.notification_description = viewModel.notifDesc
                        notifObject.activation_time = viewModel.notifTime
                        notifObject.activation_date = viewModel.notifDate
                        notifObject.isReacurring = viewModel.notifConfirmRepeating
                        notifObject.reaccuring_time = viewModel.notifinterval

                    val tagObject = Tag()

                        tagObject.tag_name = viewModel.setTag
                    if(viewModel.isEditMode){
                        taskObject.task_id = viewModel.taskID
                        notifObject.notification_id = viewModel.notifID
                        notifObject.task_id = viewModel.taskID
                        tagObject.tag_id = viewModel.tagID
                        tagObject.task_id = viewModel.taskID
                    }
                    if(viewModel.isEditMode){
                        Log.i(TAG, viewModel.taskID.toString() + tagObject.tag_id.toString() + notifObject.notification_id.toString())
                        dbRepo.updateEditedTask(taskObject, notifObject, tagObject)
                    }else{
                        dbRepo.insertAndUpdateList(taskObject, notifObject, tagObject)
                        /*dbRepo.insertTask(dbRepo.getSingleListName(viewModel.setList)[0].list_id, viewModel.title, viewModel.description, viewModel.dueDate, viewModel.setActivationDate,viewModel.setpriority)
                        sleep(500)
                        dbRepo.insertNotif(viewModel.notifTitle,viewModel.notifDesc,dbRepo.getTaskSingleName(viewModel.title)[0].task_id, viewModel.notifTime, viewModel.notifDate, viewModel.notifConfirmRepeating, viewModel.notifinterval)
                        sleep(500)
                        dbRepo.insertTag(viewModel.setTag,dbRepo.getTaskSingleName(viewModel.title)[0].task_id)
                        sleep(500)*/
                    //dbRepo.insertTask(listID, viewModel.title, viewModel.description, viewModel.dueDate, viewModel.setActivationDate,viewModel.setpriority)
                    }
                    /*dbRepo.insertTask(dbRepo.getSingleListName(viewModel.setList)[0].list_id, viewModel.title, viewModel.description, viewModel.dueDate, viewModel.setActivationDate,viewModel.setpriority)
                    sleep(500)
                    dbRepo.insertNotif(viewModel.notifTitle,viewModel.notifDesc,dbRepo.getTaskSingleName(viewModel.title)[0].task_id, viewModel.notifTime, viewModel.notifDate, viewModel.notifConfirmRepeating, viewModel.notifinterval)
                    sleep(500)
                    dbRepo.insertTag(viewModel.setTag,dbRepo.getTaskSingleName(viewModel.title)[0].task_id)
                    sleep(500)*/
                    activity?.finish()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        val noSaveBackButton: Button = view.findViewById(R.id.toListButton2)
        noSaveBackButton.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Warning!!")
                .setMessage("Unsaved data will be lost. Continue?")
                .setPositiveButton("Yes") { _, _ ->
                    activity?.finish()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        val toParamsButton: Button = view.findViewById(R.id.ParamButton2)
        toParamsButton.setOnClickListener {
            viewPager.setCurrentItem(1,false)
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
            notifDate.setText(selectedDate)
            viewModel.notifDate = selectedDate
        }, year, month, day)
        datePickerDialog.show()
    }

    companion object {}
}