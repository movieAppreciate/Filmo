package com.teamfilmo.filmo.ui.report

import androidx.fragment.app.activityViewModels
import com.teamfilmo.filmo.base.fragment.BaseFragment
import com.teamfilmo.filmo.databinding.FragmentRecommendReportBinding
import com.teamfilmo.filmo.ui.report.recyclerview.RecommendReportAdapter

class RecommendReportFragment : BaseFragment<FragmentRecommendReportBinding>(FragmentRecommendReportBinding::inflate) {
    private val reportViewModel by activityViewModels<ReportViewModel>()

    private val recommendReportAdapter by lazy {
        RecommendReportAdapter()
    }

    override fun initViews() {
        super.initViews()
        reportViewModel.recommendList.observe(viewLifecycleOwner) {
            binding.recommendRecyclerview.adapter = this.recommendReportAdapter
            recommendReportAdapter.setRecommendReport(it)
        }
    }
}
