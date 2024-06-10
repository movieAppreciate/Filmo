package com.teamfilmo.filmo.ui.report.follow

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.teamfilmo.filmo.base.fragment.BaseFragment
import com.teamfilmo.filmo.databinding.FragmentFollowingReportBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowingReportFragment : BaseFragment<FragmentFollowingReportBinding, FollowingReportViewModel, FollowingReportEffect, FollowingReportEvent>(
    FragmentFollowingReportBinding::inflate,
) {
    override val viewModel: FollowingReportViewModel by activityViewModels()

    override fun onBindLayout() {
//        viewModel.report.observe(viewLifecycleOwner) { reportList ->
//            Log.d("감상문 following", reportList.toString())
//            if (reportList != null) {
//                val followingReviewAdapter = FollowingReportAdapter()
//                followingReviewAdapter.setFollowingReportList(reportList)
//
//                binding.followReviewRecyclerview.layoutManager = LinearLayoutManager(context)
//                binding.followReviewRecyclerview.adapter = followingReviewAdapter
//            }
//        }
    }

    companion object {
        fun newInstance(): FollowingReportFragment {
            val args = Bundle()

            val fragment = FollowingReportFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
