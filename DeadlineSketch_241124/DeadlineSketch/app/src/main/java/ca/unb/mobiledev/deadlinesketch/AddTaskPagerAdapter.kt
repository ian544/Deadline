package ca.unb.mobiledev.deadlinesketch

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class AddTaskPagerAdapter(fragmentActivity: FragmentActivity)
    : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 3    //Current number of Add task pages

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CreateTaskFragment()
            1 -> SetTaskParametersFragment()
            2 -> NotificationsFragment()
            else -> throw IllegalArgumentException("Fragment Error at position: $position")
        }
    }

}
