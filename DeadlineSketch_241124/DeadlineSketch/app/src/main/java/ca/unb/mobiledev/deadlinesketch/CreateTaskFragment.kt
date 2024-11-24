package ca.unb.mobiledev.deadlinesketch

import android.app.AlertDialog
import android.app.DatePickerDialog
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
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2

class CreateTaskFragment : Fragment() {
    private val viewModel: TaskViewModel by activityViewModels()
    private lateinit var viewPager: ViewPager2
    private lateinit var title: EditText
    private lateinit var dueDate: EditText
    private lateinit var description: EditText

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewPager = (context as AddEditTaskHost).viewPager
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ceate_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        title = view.findViewById(R.id.create_TaskTitle)
        dueDate = view.findViewById(R.id.createTaskSetDate)
        description = view.findViewById(R.id.createTaskDescription)

        if(!viewModel.title.isNullOrEmpty()){
            title.setText(viewModel.title)
        }else{
            title.text = null
        }
        title.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.title = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        if(!viewModel.dueDate.isNullOrEmpty()){
            dueDate.setText(viewModel.dueDate)
        }else{
            dueDate.text = null
        }

        if(!viewModel.description.isNullOrEmpty()){
            description.setText(viewModel.description)
        }else{
            description.text = null
        }
        description.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.description = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

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

        dueDate.setOnClickListener {
            showDatePickerDialog()
        }


        val btnNext: Button = view.findViewById(R.id.ParamButton1)
        btnNext.setOnClickListener {
            viewPager.setCurrentItem(1,false)
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
        }
    }

    companion object {}

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
            dueDate.setText(selectedDate)
            viewModel.dueDate = selectedDate
        }, year, month, day)
        datePickerDialog.show()
    }
}