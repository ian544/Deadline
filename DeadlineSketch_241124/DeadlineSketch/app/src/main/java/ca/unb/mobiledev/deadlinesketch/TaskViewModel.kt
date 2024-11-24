package ca.unb.mobiledev.deadlinesketch

import android.widget.AutoCompleteTextView
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TaskViewModel : ViewModel() {
    var title: String = ""
    var dueDate: String = ""
    var description: String = ""
    var setTag: String = ""
    var setList: String = ""
    var setActivationDate: String = ""
    var setpriority: String = ""
    var notifTitle: String = ""
    var notifDesc: String = ""
    var notifDate: String = ""
    var notifTime: String = ""
    var notifConfirmRepeating: Boolean = false
    var notifinterval: String = ""
    var isEditMode: Boolean = false
    var taskID: Int = -1
    var create_FillFromDB: Boolean = true
    var param_FillFromDB: Boolean = true
    //LiveData for observing task data; waiting for database integration
    var taskList: LiveData<List<Task>> = MutableLiveData()  //replace with repository info once available
    init {
        // Initialize data; waiting for database integration
    }
}



