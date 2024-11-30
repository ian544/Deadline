package ca.unb.mobiledev.deadlinesketch

import android.widget.AutoCompleteTextView
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ca.unb.mobiledev.deadlinesketch.db.AppDatabase
import ca.unb.mobiledev.deadlinesketch.entity.Task
import ca.unb.mobiledev.deadlinesketch.repo.dbRepo

class TaskViewModel : ViewModel() {
    var title: String = ""
    var dueDate: String = ""
    var description: String = ""
    var tagID: Int = 0
    var setTag: String = ""
    var setList: String = ""
    var setActivationDate: String = ""
    var setpriority: String = ""
    var notifID: Int = 0
    var notifTitle: String = ""
    var notifDesc: String = ""
    var notifDate: String = ""
    var notifTime: String = ""
    var notifConfirmRepeating: Boolean = false
    var notifinterval: String = ""
    var isEditMode: Boolean = false
    var taskID: Int = 0
    var originalList: Int = -1
    var originalListName: String = ""
}