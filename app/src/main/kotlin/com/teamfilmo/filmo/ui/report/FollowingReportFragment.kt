package com.teamfilmo.filmo.ui.report

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.teamfilmo.filmo.databinding.FragmentFollowingReportBinding
import com.teamfilmo.filmo.ui.report.recyclerview.FollowingReportAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowingReportFragment : Fragment() {
    private lateinit var binding: FragmentFollowingReportBinding

    private val reportViewModel by activityViewModels<ReportViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentFollowingReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        reportViewModel.report.observe(viewLifecycleOwner) {
                reportList ->
            Log.d("감상문 following", reportList.toString())
            if (reportList != null) {
                val followingReviewAdapter = FollowingReportAdapter()
                followingReviewAdapter.setFollowingReportList(reportList)

                binding.followReviewRecyclerview.layoutManager = LinearLayoutManager(context)
                binding.followReviewRecyclerview.adapter = followingReviewAdapter
            }
        }
    }
}
