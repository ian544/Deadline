package ca.unb.mobiledev.deadlinesketch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChildAdapter(private val children: List<String>) : RecyclerView.Adapter<ChildAdapter.ChildViewHolder>() {
    inner class ChildViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val childTitle: TextView = itemView.findViewById(R.id.childTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.child_item, parent, false)
        return ChildViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        holder.childTitle.text = children[position]
    }

    override fun getItemCount(): Int {
        return children.size
    }
}
