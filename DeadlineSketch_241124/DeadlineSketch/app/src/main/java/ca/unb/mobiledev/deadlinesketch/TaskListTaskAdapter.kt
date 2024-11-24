package ca.unb.mobiledev.deadlinesketch

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import ca.unb.mobiledev.deadlinesketch.repo.dbRepo

class TaskListTaskAdapter(private val parentFragment: Fragment) : RecyclerView.Adapter<TaskListTaskAdapter.TaskViewHolder>(){
    private var tasks: List<Task> = listOf()
    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val taskName: TextView = itemView.findViewById(R.id.titleTextView)
        val taskDueDate: TextView = itemView.findViewById(R.id.dueDateTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.taskName.text = task.name
        holder.taskDueDate.text = task.dueDate
        holder.itemView.setOnClickListener {
            val context = parentFragment.requireContext()
            val intent = Intent(context, TaskDetails::class.java)
            intent.putExtra("taskTitle", task.name)
            intent.putExtra("taskDescription", task.description)
            intent.putExtra("taskDueDate", task.dueDate)
            intent.putExtra("taskPriority", task.priority)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    fun setTasks(newTasks: List<Task>){
        tasks = newTasks
        notifyDataSetChanged()
    }
}
