package com.teamfilmo.filmo.ui.follow.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.teamfilmo.filmo.ui.follow.tab.FollowTabInfo

class FollowPagerAdapter(
    fragment: Fragment,
    val userId: String,
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = FollowTabInfo.entries.size

    override fun createFragment(position: Int): Fragment = FollowTabInfo.getTabInfoAt(position).fragment(userId)
}
