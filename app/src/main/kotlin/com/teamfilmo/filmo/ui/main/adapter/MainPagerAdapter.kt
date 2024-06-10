package com.teamfilmo.filmo.ui.main.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.teamfilmo.filmo.ui.main.tab.MainTabInfo

class MainPagerAdapter(
    fragmentActivity: FragmentActivity,
) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return MainTabInfo.entries.size
    }

    override fun createFragment(position: Int): Fragment {
        return MainTabInfo.getTabInfoAt(position).fragment()
    }
}
