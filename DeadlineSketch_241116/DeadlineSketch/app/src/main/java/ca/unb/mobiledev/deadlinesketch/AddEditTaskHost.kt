package ca.unb.mobiledev.deadlinesketch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ca.unb.mobiledev.deadlinesketch.Repo.dbRepo

class AddEditTaskHost : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.host_add_edit_task)
        val dbRepo: dbRepo = dbRepo(this)

        //Determine Edit Mode
        val isEditMode = intent.getBooleanExtra("isEditMode",false)
        if(savedInstanceState==null){
            if(isEditMode){
                //EditMode active
                val taskID = intent.getIntExtra("taskID", 0)
                val returnedTask = dbRepo.getTaskSingle(taskID)
                //TODO: Fetch task data from DB with task ID; pass to fragment(s)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.wizardFragmentContainer, CreateTaskFragment().apply {
                        arguments = Bundle().apply {
                            putBoolean("isEditMode", true)
                            putInt("taskID", taskID)
                            //TODO: other potential DB details, etc.
                        }
                    })
                    .commit()
            }else {
                //Add Task Mode
                supportFragmentManager.beginTransaction()
                    .replace(R.id.wizardFragmentContainer, CreateTaskFragment().apply {
                        arguments = Bundle().apply {
                            putBoolean("isEditMode", false)
                        }
                    })
                    .commit()
                //May be a way to trim code; leaving redundancy for now since I don't know what works and what doesn't
            }
        }
    }
}
