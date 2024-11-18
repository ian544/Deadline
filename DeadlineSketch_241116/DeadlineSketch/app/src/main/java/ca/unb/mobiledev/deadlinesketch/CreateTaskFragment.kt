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
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController

class CreateTaskFragment : Fragment() {
    private val viewModel: TaskViewModel by activityViewModels()
    private lateinit var title: EditText
    private lateinit var dueDate: EditText
    private lateinit var description: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ceate_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        title = view.findViewById(R.id.task_title)
        dueDate = view.findViewById(R.id.notificationSetDate)
        description = view.findViewById(R.id.notificationdescription)

        val isEditMode = arguments?.getBoolean("isEditMode") ?: false
        if(viewModel.create_FillFromDB){
            viewModel.isEditMode = isEditMode
        }
        if(!viewModel.isEditMode){
            title.setText(viewModel.title)
            dueDate.setText(viewModel.dueDate)
            description.setText(viewModel.description)
        }

        if(viewModel.isEditMode && viewModel.create_FillFromDB){
            val taskID = arguments?.getInt("taskID") ?: 0
            viewModel.taskID = taskID
            //TODO: Fetch task data from DB and populate fields
            val taskName = "Placeholder Name" //Replace with fetched data
            val taskDueDate = "1000/01/01" //Replace with fetched data
            val taskDescript = "This is a placeholder description" //Replace with fetched data

            title.setText(taskName)
            dueDate.setText(taskDueDate)
            description.setText(taskDescript)

            viewModel.title = title.text.toString()
            viewModel.dueDate = dueDate.text.toString()
            viewModel.description = description.text.toString()
            viewModel.create_FillFromDB = false
        }

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

        dueDate.setOnClickListener { showDatePickerDialog() }
        viewModel.notifDate = dueDate.toString()

        val btnNext: Button = view.findViewById(R.id.ParamButton1)
        btnNext.setOnClickListener {
            viewModel.title = title.text.toString()
            viewModel.dueDate = dueDate.toString()
            viewModel.description = description.text.toString()
            parentFragmentManager.beginTransaction()
                .replace(R.id.wizardFragmentContainer, SetTaskParametersFragment())
                .commit()
            //findNavController().navigate(R.id.action_createTaskFragment_to_setTaskParametersFragment)
        }

        val noSaveBackButton: Button = view.findViewById(R.id.toListButton)
        noSaveBackButton.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Warning!!")
                .setMessage("Unsaved data will be lost. Continue?")
                .setPositiveButton("Yes") { _, _ ->
                    activity?.finish()
                }
                .setNegativeButton("Cancel", null)
                .show()
            //findNavController().navigate(R.id.action_createTaskFragment_to_listFragment)
        }
    }

    companion object {
        //fun newInstance() = CeateTaskFragment()
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
            dueDate.setText(selectedDate)
        }, year, month, day)
        datePickerDialog.show()
    }
}