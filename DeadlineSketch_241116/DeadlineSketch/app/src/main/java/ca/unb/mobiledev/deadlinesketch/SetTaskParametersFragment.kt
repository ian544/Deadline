package ca.unb.mobiledev.deadlinesketch

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
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


class SetTaskParametersFragment : Fragment() {
    private val viewModel: TaskViewModel by activityViewModels()
    private lateinit var taskTitle: TextView
    private lateinit var taskDueDate: TextView
    private lateinit var setTag: AutoCompleteTextView
    private lateinit var setList: Spinner
    private lateinit var setActivationDate: EditText
    private lateinit var setpriority: Spinner
    private var positionList: Int = -1
    private var positionPriority: Int = -1

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
        setTag = view.findViewById(R.id.tag_auto_complete)
        setList = view.findViewById(R.id.listSpinner)
        setActivationDate = view.findViewById(R.id.activateDate)
        setpriority = view.findViewById(R.id.list_spinner)

        taskTitle.text = viewModel.title
        taskDueDate.text = viewModel.dueDate

        val tags = arrayOf("Work", "Personal", "School") // Example data
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, tags)
        setTag.setAdapter(adapter)

        val taskLists = arrayOf("ToDo", "Archive", "Next Week", "Planning")
        val listAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, taskLists)
        listAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        setList.adapter = listAdapter

        val taskPriorities = arrayOf("Low", "Neutral/Default", "Medium", "High", "Urgent/Emergency")
        val priorityAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, taskPriorities)
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        setpriority.adapter = priorityAdapter

        if(viewModel.isEditMode && viewModel.param_FillFromDB){
            //TODO: Use viewModel.taskID to populate fields and choices as necessary
            val dbTag = "Tag" //Replace with fetched data
            val dbList = "List" //Replace with fetched data
            val dbActivationDate = "1000/01/01" //Replace with fetched data
            val dbPriority = "Priority" //Replace with fetched data

            positionList = listAdapter.getPosition(dbList)
            positionPriority = priorityAdapter.getPosition(dbPriority)

            setTag.setText(dbTag)
            if(positionList >= 0){ setList.setSelection(positionList) }
            if(positionPriority >= 0){ setpriority.setSelection(positionPriority) }
            setActivationDate.setText(dbActivationDate)

            viewModel.setTag = setTag.text.toString()
            viewModel.setList = setList.getItemAtPosition(positionList).toString()
            viewModel.setpriority = setpriority.getItemAtPosition(positionPriority).toString()
            viewModel.setActivationDate = setActivationDate.text.toString()
            viewModel.param_FillFromDB = false
        }

        //Reads values from ViewModel when DB values have already been loaded, or when cycling between Create and Params
        if(!viewModel.param_FillFromDB){
            setTag.setText(viewModel.setTag)
            positionList = listAdapter.getPosition(viewModel.setList)
            if(positionList >= 0){ setList.setSelection(positionList) }

            positionPriority = priorityAdapter.getPosition(viewModel.setpriority)
            if(positionPriority >= 0){ setpriority.setSelection(positionPriority) }

            setActivationDate.setText(viewModel.setActivationDate)
        }

        setTag.setOnItemClickListener { parent, _, position, _ ->
            val selectedTag = parent.getItemAtPosition(position).toString()
            viewModel.setTag = selectedTag
        }

        setList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedList = parent.getItemAtPosition(position).toString()
                viewModel.setList = selectedList
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        setActivationDate.setOnClickListener { showDatePickerDialog() }
        viewModel.setActivationDate = setActivationDate.toString()

        setpriority.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedPriority = parent.getItemAtPosition(position).toString()
                viewModel.setpriority = selectedPriority
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        val backToCreate: Button = view.findViewById(R.id.toCreateTask1)
        backToCreate.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.wizardFragmentContainer,CreateTaskFragment())
                .commit()
            //findNavController().navigate(R.id.action_setTaskParametersFragment_to_createTaskFragment)
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
            //findNavController().navigate(R.id.action_setTaskParametersFragment_to_listFragment)
        }

        val setNotifications: Button = view.findViewById(R.id.setNotifications)
        setNotifications.setOnClickListener{
            parentFragmentManager.beginTransaction()
                .replace(R.id.wizardFragmentContainer, NotificationsFragment())
                .addToBackStack(null)
                .commit()
            //findNavController().navigate(R.id.action_setTaskParametersFragment_to_notificationsFragment)
        }

        val submitTask: Button = view.findViewById(R.id.submit_button)
        submitTask.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setMessage("Submit Task to DataBase and Exit?")
                .setPositiveButton("Yes") { _, _ ->
                    activity?.finish()
                }
                .setNegativeButton("Cancel", null)
                .show()
            //findNavController().navigate(R.id.action_setTaskParametersFragment_to_listFragment)
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
        }, year, month, day)
        datePickerDialog.show()
    }

    companion object {
        //fun newInstance() = SetTaskParametersFragment()
    }
}