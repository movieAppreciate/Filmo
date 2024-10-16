package com.teamfilmo.filmo.ui.report.all

import android.os.Bundle
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teamfilmo.filmo.base.fragment.BaseFragment
import com.teamfilmo.filmo.databinding.FragmentAllMovieReportBinding
import com.teamfilmo.filmo.ui.report.adapter.AllMovieReportAdapter
import com.teamfilmo.filmo.ui.report.adapter.MovieInfoAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllMovieReportFragment :
    BaseFragment<FragmentAllMovieReportBinding, AllMovieReportViewModel, AllMovieReportEffect, AllMovieReportEvent>(
        FragmentAllMovieReportBinding::inflate,
    ) {
    override val viewModel: AllMovieReportViewModel by viewModels()
    private val navController by lazy { findNavController() }

    private var currentPage: Int = 1
    val allMovieReportAdapter by lazy {
        AllMovieReportAdapter()
    }

    val movieInfoAdapter by lazy {
        MovieInfoAdapter()
    }

    override fun handleEffect(effect: AllMovieReportEffect) {
        when (effect) {
            is AllMovieReportEffect.RegistLike -> {
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
            else -> {}
        }
    }

    override fun onBindLayout() {
        childFragmentManager.commit {
            setReorderingAllowed(true)

            with(binding) {
                allMovieReportRecyclerview.addOnScrollListener(
                    object : RecyclerView.OnScrollListener() {
                        override fun onScrolled(
                            recyclerView: RecyclerView,
                            dx: Int,
                            dy: Int,
                        ) {
                            super.onScrolled(recyclerView, dx, dy)

                            val lastVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                            val itemTotalCount = recyclerView.adapter?.itemCount?.minus(1)

                            // 스크롤이 끝에 도달했는지 확인
                            if (lastVisibleItemPosition == itemTotalCount) {
                                currentPage++
                                viewModel.handleEvent(AllMovieReportEvent.LoadNextPageReport(currentPage))
                            }
                        }
                    },
                )
                allMovieReportRecyclerview.adapter = allMovieReportAdapter
                movieRecyclerview.adapter = movieInfoAdapter
                swiperefresh.setOnRefreshListener {
                    viewModel.handleEvent(AllMovieReportEvent.RefreshReport)
                    swiperefresh.isRefreshing = false
                }
            }

            lifecycleScope.launch {
                launch {
                    viewModel.allMovieReportList.collect {
                        binding.allMovieReportRecyclerview.apply {
                            allMovieReportAdapter.setReportInfo(viewModel.allMovieReportList.value)
                        }
                    }
                }
                launch {
                    viewModel.upcomingMovieList.collect { movieInfoList ->
                        binding.movieRecyclerview.apply {
                            movieInfoAdapter.setMovieInfoList(movieInfoList)
                        }
                    }
                }
            }
        }
        movieInfoAdapter.itemClick =
            object : MovieInfoAdapter.ItemClick {
                val movie = movieInfoAdapter.movieList

                override fun onClick(position: Int) {
                    val movieId = movie[position].id
                    navigateToMovieDetail(movieId)
                }
            }

        allMovieReportAdapter.itemClick =
            object : AllMovieReportAdapter.ItemClick {
                override fun onClick(position: Int) {
                    val report = allMovieReportAdapter.reportList[position]
                    navigateToBodyReport(report.reportId)
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

    private fun navigateToMovieDetail(movieId: Int) {
        val action = AllMovieReportFragmentDirections.navigateToMovieDetail(movieId)
        navController.navigate(action)
    }

    private fun navigateToBodyReport(reportId: String) {
        val action = AllMovieReportFragmentDirections.navigateToBody(reportId)
        navController.navigate(action)
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
