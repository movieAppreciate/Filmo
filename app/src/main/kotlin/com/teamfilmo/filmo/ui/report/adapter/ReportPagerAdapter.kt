package com.teamfilmo.filmo.ui.report.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.teamfilmo.filmo.ui.report.all.AllMovieReportFragment

class ReportPagerAdapter(
    fragment: Fragment,
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 1

    override fun createFragment(position: Int): Fragment {
        return AllMovieReportFragment.newInstance()
    }
}
