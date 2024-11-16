package ca.unb.mobiledev.deadlinesketch

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
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


class NotificationsFragment : Fragment() {
    private val viewModel: TaskViewModel by activityViewModels()
    private lateinit var notifTitle: EditText
    private lateinit var notifDesc: EditText
    private lateinit var notifDate: EditText
    private lateinit var notifTime: EditText
    private lateinit var notifConfirmRepeating: CheckBox
    private lateinit var notifinterval: Spinner

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
        notifTime = view.findViewById(R.id.notificationTime)
        notifConfirmRepeating = view.findViewById(R.id.notificationcheckBox)
        notifinterval = view.findViewById(R.id.list_spinner)

        val intervals = arrayOf("1 Day", "2 Days", "3 Days", "4 Days", "5 Days","6 Days","7 Days")
        val intervalAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, intervals)
        intervalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        notifinterval.adapter = intervalAdapter

        if(viewModel.isEditMode){
            //TODO: Pull data from DB for fields
            val dbNTitle = "N_Title" //Replace with fetched data
            val dbNDesc = "N_Desc" //Replace with fetched data
            val dbNDate = "1000/01/01" //Replace with fetched data
            val dbNTime = "00:00" //Replace with fetched data
            val dbRepeating = false //Replace with fetched data
            val dbInterval = "interval" //Replace with fetched data

            val positionInterval = intervalAdapter.getPosition(dbInterval)

            notifTitle.setText(dbNTitle)
            notifDesc.setText(dbNDesc)
            notifDate.setText(dbNDate)
            notifTime.setText(dbNTime)
            notifConfirmRepeating.isChecked = dbRepeating
            if(positionInterval >= 0) { notifinterval.setSelection(positionInterval) }
        }

        notifDate.setOnClickListener { showDatePickerDialog() }
        viewModel.notifDate = notifDate.toString()

        notifTime.setOnClickListener { showTimePickerDialog() }
        viewModel.notifTime = notifTime.toString()

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

        val submitnotification: Button = view.findViewById(R.id.submit_button)
        submitnotification.setOnClickListener {
            viewModel.notifTitle = notifTitle.text.toString()
            viewModel.notifDesc = notifDesc.text.toString()
            AlertDialog.Builder(requireContext())
                .setMessage("Submit Notification?")
                .setPositiveButton("Yes") { _, _ ->
                    parentFragmentManager.popBackStackImmediate("SetTaskParametersFragment", 0)
                }
                .setNegativeButton("Cancel", null)
                .show()
            //findNavController().navigate(R.id.action_notificationsFragment_to_setTaskParametersFragment)
            //confirm notification submission popup (probably)
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
            //findNavController().navigate(R.id.action_notificationsFragment_to_listFragment)
        }

        val noSavetoParamsButton: Button = view.findViewById(R.id.ParamButton2)
        noSavetoParamsButton.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Warning!!")
                .setMessage("Some unsaved data may be lost. Continue?")
                .setPositiveButton("Yes") { _, _ ->
                    parentFragmentManager.popBackStackImmediate("SetTaskParametersFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                }
                .setNegativeButton("Cancel", null)
                .show()
            //findNavController().navigate(R.id.action_notificationsFragment_to_setTaskParametersFragment)
            //notify user of lost data
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
        }, year, month, day)
        datePickerDialog.show()
    }

    //Method causes fatal error; not sure why yet.
    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
            val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            notifTime.setText(selectedTime)
        }, hour, minute, true)

        timePickerDialog.show()
    }

    companion object {
        //fun newInstance() = NotificationsFragment()
    }
}