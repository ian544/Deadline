package ca.unb.mobiledev.deadlinesketch

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SettingsDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_settings, null)

        builder.setView(view)
            .setTitle("Settings")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        val settingsList = listOf(
            SettingItem("User Preferences", listOf("Privacy and Security", "Account Settings", "Data and Storage", "Appearance")),
            SettingItem("Calendar", listOf("Connect/Disconnect", "Sync Frequency", "Manual Sync")),
            SettingItem("User Guide"),
            SettingItem("System Defaults", listOf("Task Defaults", "Task Recurrence Defaults", "Task Completion Settings")),
            SettingItem("Notifications", listOf("Notification Type", "Notification Frequency", "Visual Settings"))
        )
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = ParentAdapter(settingsList)

        return builder.create()
    }
}