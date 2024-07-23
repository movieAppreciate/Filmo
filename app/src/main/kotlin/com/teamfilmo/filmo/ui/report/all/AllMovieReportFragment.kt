package com.teamfilmo.filmo.ui.report.all

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.teamfilmo.filmo.base.fragment.BaseFragment
import com.teamfilmo.filmo.databinding.FragmentAllMovieReportBinding
import com.teamfilmo.filmo.ui.report.adapter.AllMovieReportAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllMovieReportFragment :
    BaseFragment<FragmentAllMovieReportBinding, AllMovieReportViewModel, AllMovieReportEffect, AllMovieReportEvent>(
        FragmentAllMovieReportBinding::inflate,
    ) {
    override val viewModel: AllMovieReportViewModel by viewModels()

    val allMovieReportAdapter by lazy {
        AllMovieReportAdapter()
    }

    private fun onRefresh() {
        binding.swiperefresh.setOnRefreshListener {
            viewModel.handleEvent(AllMovieReportEvent.RefreshReport)
            binding.swiperefresh.isRefreshing = false
        }
    }

    override fun handleEffect(effect: AllMovieReportEffect) {
        when (effect) {
            is AllMovieReportEffect.RegistLike -> {
                // 좋아요 아이콘 변경
                allMovieReportAdapter.updateLikeState(effect.reportId, true)
            }

            is AllMovieReportEffect.CancelLike -> {
                allMovieReportAdapter.updateLikeState(effect.reportId, false)
            }

            is AllMovieReportEffect.CountLike ->
                lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.likeState.collect {
                            allMovieReportAdapter.updateLikeCount(effect.reportId, effect.likeCount)
                        }
                    }
                }
            is AllMovieReportEffect.RegistBookmark ->
                lifecycleScope.launch {
                    allMovieReportAdapter.updateBookmarkState(effect.reportId, true)
                }
            is AllMovieReportEffect.DeleteBookmark ->
                allMovieReportAdapter.updateBookmarkState(effect.reportId, false)
            is AllMovieReportEffect.RefreshReport ->
                allMovieReportAdapter.setReportInfo(effect.reportList)
        }
    }

    override fun onBindLayout() {
        childFragmentManager.commit {
            setReorderingAllowed(true)

            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.allMovieReportList.collect {
                        binding.allMovieReportRecyclerview.apply {
                            adapter = allMovieReportAdapter
                            allMovieReportAdapter.setReportInfo(viewModel.allMovieReportList.value)
                        }
                    }
                }
            }
            onRefresh()
        }

        allMovieReportAdapter.itemClick =
            object : AllMovieReportAdapter.ItemClick {
                override fun onClick(position: Int) {
                    Toast.makeText(context, "감상문 클릭", Toast.LENGTH_SHORT).show()
                }

                override fun onLikeClick(position: Int) {
                    val report = allMovieReportAdapter.reportList[position]
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.handleEvent(AllMovieReportEvent.ClickLike(report.reportId))
                    }
                }

                override fun onBookmarkClick(position: Int) {
                    val report = allMovieReportAdapter.reportList[position]
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.handleEvent(AllMovieReportEvent.ClickBookmark(report.reportId))
                    }
                }
            }
    }

    companion object {
        fun newInstance(): AllMovieReportFragment {
            val args = Bundle()

            val fragment = AllMovieReportFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
