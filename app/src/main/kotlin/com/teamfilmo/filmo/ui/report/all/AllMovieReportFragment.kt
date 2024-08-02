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
import com.teamfilmo.filmo.ui.report.adapter.MovieInfoAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class AllMovieReportFragment :
    BaseFragment<FragmentAllMovieReportBinding, AllMovieReportViewModel, AllMovieReportEffect, AllMovieReportEvent>(
        FragmentAllMovieReportBinding::inflate,
    ) {
    override val viewModel: AllMovieReportViewModel by viewModels()
    val allMovieReportAdapter by lazy {
        AllMovieReportAdapter()
    }

    val movieInfoAdapter by lazy {
        MovieInfoAdapter()
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

            binding.apply {
                allMovieReportRecyclerview.adapter = allMovieReportAdapter
                movieRecyclerview.adapter = movieInfoAdapter
                swiperefresh.setOnRefreshListener {
                    viewModel.handleEvent(AllMovieReportEvent.RefreshReport)
                    swiperefresh.isRefreshing = false
                }
            }

            Toast.makeText(context, "all movie report fragment", Toast.LENGTH_SHORT).show()
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    Timber.d("실행됨")
                    viewModel.allMovieReportList.collect {
                        binding.allMovieReportRecyclerview.apply {
                            Timber.d("어댑터")
                            adapter = allMovieReportAdapter
                            allMovieReportAdapter.setReportInfo(viewModel.allMovieReportList.value)
                        }
                    }
                }
            }
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.upcomingMovieList.collect { movieInfoList ->
                        binding.movieRecyclerview.apply {
                            adapter = movieInfoAdapter
                            movieInfoAdapter.setMovieInfoList(movieInfoList)
                        }
                    }
                }
            }
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
