package com.teamfilmo.filmo.ui.report.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.teamfilmo.filmo.ui.report.tab.ReportTabInfo

class ReportPagerAdapter(
    fragment: Fragment,
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return ReportTabInfo.entries.size
    }

    override fun createFragment(position: Int): Fragment {
        return ReportTabInfo.getTabInfoAt(position).fragment()
    }
}
