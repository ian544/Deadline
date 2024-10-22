package ca.unb.mobiledev.deadlinesketch

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
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.material.button.MaterialButton

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ListFragment : Fragment(R.layout.fragment_list) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titleButton: Button = view.findViewById(R.id.ListButton)
        val fragmentName = arguments?.getString("name") ?: "Default Name"
        titleButton.text = fragmentName

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

        val addTaskButton: MaterialButton = view.findViewById(R.id.NewTaskButton)
        addTaskButton.setOnClickListener {
            val intent = Intent(activity, AddTaskActivity::class.java)
            startActivity(intent)
        }
    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}