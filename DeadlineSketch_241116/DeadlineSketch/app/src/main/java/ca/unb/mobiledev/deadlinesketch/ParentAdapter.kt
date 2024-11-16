package ca.unb.mobiledev.deadlinesketch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ParentAdapter(private val settingsList: List<SettingItem>) :
    RecyclerView.Adapter<ParentAdapter.ParentViewHolder>() {

    inner class ParentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.settingTitle)
        val childRecyclerView: RecyclerView = itemView.findViewById(R.id.childRecyclerView)
        val expandIcon: ImageView = itemView.findViewById(R.id.expandIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.parent_item, parent, false)
        return ParentViewHolder(view)
    }

    override fun onBindViewHolder(holder: ParentViewHolder, position: Int) {
        val settingItem = settingsList[position]
        holder.title.text = settingItem.title
        holder.expandIcon.setImageResource(
            if(settingItem.isExpanded)
                R.drawable.ic_expand_less
            else
                R.drawable.ic_expand_more
        )
        holder.title.setOnClickListener {
            settingItem.isExpanded = !settingItem.isExpanded
            notifyItemChanged(position)
        }

        if(settingItem.isExpanded) {
            holder.childRecyclerView.visibility = View.VISIBLE
            holder.childRecyclerView.adapter = ChildAdapter(settingItem.children)
            holder.childRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
        }else{
            holder.childRecyclerView.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int {
        return settingsList.size
    }
}
