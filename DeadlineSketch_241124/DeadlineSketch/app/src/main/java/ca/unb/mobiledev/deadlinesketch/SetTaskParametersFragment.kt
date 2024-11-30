package ca.unb.mobiledev.deadlinesketch

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.core.view.get
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import ca.unb.mobiledev.deadlinesketch.repo.dbRepo


class SetTaskParametersFragment : Fragment() {
    private val viewModel: TaskViewModel by activityViewModels()
    private lateinit var viewPager: ViewPager2
    private lateinit var taskTitle: TextView
    private lateinit var taskDueDate: TextView
    private lateinit var setTag: Spinner
    private lateinit var setList: Spinner
    private lateinit var setActivationDate: EditText
    private lateinit var setpriority: Spinner
    private var positionList: Int = -1
    private var positionTag: Int = -1
    private var positionPriority: Int = -1
    private  lateinit var dbRepo: dbRepo
    private val tags = mutableListOf<String>()
    private lateinit var tagAdapter: ArrayAdapter<String>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dbRepo = dbRepo(context)
        viewPager = (context as AddEditTaskHost).viewPager
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_set_task_parameters, container, false)
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

        taskTitle = view.findViewById(R.id.tvTitleValue)
        taskDueDate = view.findViewById(R.id.tvDueDateValue)
        setTag = view.findViewById(R.id.tagSpinner)
        setList = view.findViewById(R.id.listSpinner)
        setActivationDate = view.findViewById(R.id.activateDate)
        setpriority = view.findViewById(R.id.list_spinner)

        if(!viewModel.title.isNullOrEmpty()){
            taskTitle.text = viewModel.title
        }else{
            taskTitle.text = null
        }
        if(!viewModel.dueDate.isNullOrEmpty()){
            taskDueDate.text = viewModel.dueDate
            val status = viewModel.dueDate.isNullOrEmpty()
            Log.i(TAG, status.toString()+", "+viewModel.dueDate.toString())
        }else{
            taskDueDate.text = null
            val status = viewModel.dueDate.isNullOrEmpty()
            Log.i(TAG, status.toString()+", "+viewModel.dueDate.toString())
        }

        loadDBTags()

        var listList = dbRepo.getList()
        var i = 0
        var lists = arrayOf<String>()
        while(i < listList.size){
            lists = lists + listList[i].list_name
            i = i+1;
        }
        val taskLists = lists
        val listAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, taskLists)
        listAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        setList.adapter = listAdapter

        val taskPriorities = arrayOf("None", "Low", "Medium", "High", "Emergency")
        val priorityAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, taskPriorities)
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        setpriority.adapter = priorityAdapter

        if(!viewModel.setTag.isNullOrEmpty()){

            positionTag = tagAdapter.getPosition(viewModel.setTag)
            Log.i(TAG, "Testing viewmodel: "+viewModel.setTag+" and position: "+positionTag)
            if(positionTag >= 0){
                setTag.setSelection(positionTag)
            }
        }else{
            Log.i(TAG, "Tag record missing from possible tags.")
        }

        if(!viewModel.setList.isNullOrEmpty()){
            positionList = listAdapter.getPosition(viewModel.setList)
            if(positionList >= 0){
                setList.setSelection(positionList)
            }else{
                Log.i(TAG, "List record missing from possible lists.")
            }
        }

        if(!viewModel.setActivationDate.isNullOrEmpty()){
            setActivationDate.setText(viewModel.setActivationDate)
        }else{
            setActivationDate.text = null
        }

        if(!viewModel.setpriority.isNullOrEmpty()){
            positionPriority = priorityAdapter.getPosition(viewModel.setpriority)
            if(positionPriority >= 0){
                setpriority.setSelection(positionPriority)
            }else{
                Log.i(TAG, "Invalid priority listing.")
            }
        }

        setTag.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedTag = parent.getItemAtPosition(position).toString()
                if(selectedTag=="New_Tag"){
                    addTagDialog()
                }else{
                    if(selectedTag!=""){
                        viewModel.setTag = selectedTag
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        setList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedList = parent.getItemAtPosition(position).toString()
                viewModel.setList = selectedList
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        setActivationDate.setOnClickListener { showDatePickerDialog() }

        setpriority.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedPriority = parent.getItemAtPosition(position).toString()
                viewModel.setpriority = selectedPriority
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        val backToCreate: Button = view.findViewById(R.id.toCreateTask1)
        backToCreate.setOnClickListener {
            viewPager.setCurrentItem(0,false)
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

        val setNotifications: Button = view.findViewById(R.id.setNotifications)
        setNotifications.setOnClickListener{
            viewPager.setCurrentItem(2,false)
        }
    }

    override fun onResume() {
        super.onResume()
        if(!viewModel.title.isNullOrEmpty()){
            taskTitle.text = viewModel.title
        }else{
            taskTitle.text = null
        }
        if(!viewModel.dueDate.isNullOrEmpty()){
            taskDueDate.text = viewModel.dueDate
            val status = viewModel.dueDate.isNullOrEmpty()
            Log.i(TAG, status.toString()+", "+viewModel.dueDate.toString())
        }else{
            taskDueDate.text = null
            val status = viewModel.dueDate.isNullOrEmpty()
            Log.i(TAG, status.toString()+", "+viewModel.dueDate.toString())
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
            setActivationDate.setText(selectedDate)
            viewModel.setActivationDate = selectedDate
        }, year, month, day)
        datePickerDialog.show()
    }

    private fun loadDBTags(){
        var tagList = dbRepo.getTagList()
        tags.clear()
        var j = 0
        tags.add("")
        tags.add("New_Tag")
        while(j < tagList.size){
            tags.add(tagList[j].tag_name)
            j += 1
        }
        updateTagSpinner(tags)
    }

    private fun addTag(tagName: String){
        tags.add(tagName)
        viewModel.setTag = tagName
        updateTagSpinner(tags)
        val positionTag = tagAdapter.getPosition(tagName)
        if (positionTag >= 0) { setTag.setSelection(positionTag) }
    }

    private fun addTagDialog(){
        val builder = AlertDialog.Builder(requireContext())
        val input = EditText(requireContext())
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)
        builder.setPositiveButton("Add"){ _, _ ->
            val newTagName = input.text.toString()
            if (newTagName.isNotEmpty()){
                addTag(newTagName)
            }
        }
        builder.setNegativeButton("Cancel"){ dialog, _ -> dialog.cancel()}
        builder.show()
    }

    private fun updateTagSpinner(allTags: List<String>){
        tagAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item,allTags)
        tagAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        setTag.adapter = tagAdapter
    }

    companion object {}
}