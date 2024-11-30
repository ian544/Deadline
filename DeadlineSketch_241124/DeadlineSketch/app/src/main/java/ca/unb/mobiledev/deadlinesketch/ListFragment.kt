package ca.unb.mobiledev.deadlinesketch

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity.LAYOUT_INFLATER_SERVICE
import androidx.appcompat.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ca.unb.mobiledev.deadlinesketch.entity.list
import ca.unb.mobiledev.deadlinesketch.repo.dbRepo
import com.google.android.material.button.MaterialButton


class ListFragment : Fragment(R.layout.fragment_list) {
    private lateinit var dbRepo: dbRepo
    private lateinit var fragmentName: String
    private lateinit var curList: list
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbRepo = dbRepo(requireActivity().applicationContext)

        val titleButton: Button = view.findViewById(R.id.ListTitle)
        fragmentName = arguments?.getString("name") ?: ""
        titleButton.text = fragmentName
         curList = dbRepo.getSingleListName(fragmentName)[0]

        val logo: ImageView = view.findViewById(R.id.logo_home)
        logo.setOnClickListener {
            val intent = Intent(activity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        val gearIcon: ImageView = view.findViewById(R.id.gearIcon)
        gearIcon.setOnClickListener { popupView ->
            val inflater: LayoutInflater = context?.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val contentView: View = inflater.inflate(R.layout.popup_layout, null)
            val popupWindow = PopupWindow(
                contentView,
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )

            popupWindow.setBackgroundDrawable(GradientDrawable().apply {
                setColor(Color.WHITE)
                setStroke(2, Color.BLACK)
            })
            popupWindow.isFocusable = true
            popupWindow.showAsDropDown(popupView, 0, 0)
        }

        val filterButton: MaterialButton = view.findViewById(R.id.FilterButton)
        filterButton.setOnClickListener { popupView ->
            val popupMenu = PopupMenu(requireContext(), popupView)
            popupMenu.menuInflater.inflate(R.menu.filterdropdown, popupMenu.menu)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                popupMenu.gravity = Gravity.END
            }
            popupMenu.setOnMenuItemClickListener { item ->
                // Handle item selection dynamically
                true
            }
            // Show the popup with the custom background
            popupMenu.show()
        }

        updateDisplay(view, fragmentName, requireContext())

        val addTaskButton: MaterialButton = view.findViewById(R.id.toListButton)
        addTaskButton.setOnClickListener {
            val intent = Intent(requireContext(), AddEditTaskHost::class.java)
            intent.putExtra("isEditMode",false)
            startActivity(intent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onResume() {
        super.onResume()
        view?.let { updateDisplay(it, fragmentName, requireContext()) }
    }

    companion object {
        @JvmStatic
        fun newInstance(name: String): ListFragment {
            return ListFragment().apply {
                arguments = Bundle().apply {
                    putString("name", name)
                }
            }
        }
    }
    fun updateDisplay(view: View, listName: String, context: Context){
        val recyclerView: RecyclerView = view.findViewById(R.id.taskListRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val dataTask = LoadTaskFragment(requireActivity(), listName, this)
        dataTask.setRecyclerView(recyclerView)
        dataTask.execute()
        var taskList = dbRepo.getTaskList(curList.list_id)
        recyclerView.adapter = TaskListTaskAdapter(this, taskList)
    }
}
