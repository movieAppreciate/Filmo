package com.teamfilmo.filmo.ui.follow.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.teamfilmo.filmo.ui.follow.tab.FollowTabInfo

class FollowPagerAdapter(
    fragment: Fragment,
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return FollowTabInfo.entries.size
    }

    override fun createFragment(position: Int): Fragment {
        return FollowTabInfo.getTabInfoAt(position).fragment()
    }
}
