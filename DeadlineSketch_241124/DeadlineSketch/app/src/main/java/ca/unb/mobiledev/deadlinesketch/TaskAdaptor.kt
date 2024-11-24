package ca.unb.mobiledev.deadlinesketch

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ca.unb.mobiledev.deadlinesketch.entity.Task

class TaskAdapter(private val parentActivity: Activity, private val mDataset: List<Task>) :
    RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val dueDateTextView: TextView = itemView.findViewById(R.id.dueDateTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = mDataset[position]
        holder.titleTextView.text = task.title
        holder.dueDateTextView.text = task.due_date
        holder.itemView.setOnClickListener {
            val intent = Intent(parentActivity, DetailActivity::class.java)
            intent.putExtra("taskTitle", task.title)
            intent.putExtra("taskDescription", task.description)
            intent.putExtra("taskDueDate", task.due_date)
            parentActivity.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return mDataset.size
    }

//      Needs adjustment to var over val for mDataset; also notify choice inefficient; possibly use notifyItemChanged instead
//    fun setItems(newTasks: ArrayList<Task>){
//        mDataset = newTasks
//        notifyDataSetChanged()
//    }

    companion object {
        private const val TAG = "TaskAdaptor"
    }
}

