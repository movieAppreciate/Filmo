package com.teamfilmo.filmo.ui.report

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.teamfilmo.filmo.base.fragment.BaseFragment
import com.teamfilmo.filmo.databinding.FragmentReportBinding
import com.teamfilmo.filmo.ui.report.adapter.ReportPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportFragment :
    BaseFragment<FragmentReportBinding, ReportViewModel, ReportEffect, ReportEvent>(
        FragmentReportBinding::inflate,
    ) {
    override val viewModel: ReportViewModel by viewModels()

    override fun onBindLayout() {
        binding.viewPager.adapter = ReportPagerAdapter(this)
        binding.viewPager.isUserInputEnabled = false
    }

    companion object {
        fun newInstance(): ReportFragment {
            val args = Bundle()
            val fragment = ReportFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
