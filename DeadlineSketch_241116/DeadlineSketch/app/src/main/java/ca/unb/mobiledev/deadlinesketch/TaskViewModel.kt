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
    var title: String? = null
    var dueDate: String? = null
    var description: String? = null
    var setTag: String? = null
    var setList: String? = null
    var setActivationDate: String? = null
    var setpriority: String? = null
    var notifTitle: String? = null
    var notifDesc: String? = null
    var notifDate: String? = null
    var notifTime: String? = null
    var notifConfirmRepeating: Boolean = false
    var notifinterval: String? = null
    var isEditMode: Boolean = false
    var taskID: Int? = null
    var create_FillFromDB: Boolean = true
    var param_FillFromDB: Boolean = true
    //LiveData for observing task data; waiting for database integration
    var taskList: LiveData<List<Task>> = MutableLiveData()  //replace with repository info once available
    init {
        // Initialize data; waiting for database integration
    }
}



