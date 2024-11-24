package ca.unb.mobiledev.deadlinesketch

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ListPagerAdapter (fragmentActivity: FragmentActivity, private val fragmentNames: List<String>)
    : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = fragmentNames.size

    override fun createFragment(position: Int): Fragment {
        return ListFragment.newInstance(fragmentNames[position])
    }
}