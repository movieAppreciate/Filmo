package com.teamfilmo.filmo.ui.report

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import com.teamfilmo.filmo.base.fragment.BaseFragment
import com.teamfilmo.filmo.databinding.FragmentAllMovieReportBinding
import com.teamfilmo.filmo.domain.model.report.all.ReportItem
import com.teamfilmo.filmo.ui.report.adapter.AllMovieReportAdapter
import com.teamfilmo.filmo.ui.report.adapter.MovieInfoAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllMovieReportFragment :
    BaseFragment<FragmentAllMovieReportBinding, AllMovieReportViewModel, AllMovieReportEffect, AllMovieReportEvent>(
        FragmentAllMovieReportBinding::inflate,
    ) {
    override val viewModel: AllMovieReportViewModel by activityViewModels()

    private val navController by lazy { findNavController() }

    private val allMovieReportAdapter by lazy {
        adapter ?: AllMovieReportAdapter().also { adapter = it }
    }
    val movieInfoAdapter by lazy {
        MovieInfoAdapter()
    }
    val args: AllMovieReportFragmentArgs by navArgs()

    override fun onResume() {
        super.onResume()
        // 댓글 수에 변화가 있는 경우
        if (args.changedReplyCount != -1) {
            allMovieReportAdapter.updateReplyCount(args.updatedReportId, args.changedReplyCount)
        }
        // 좋아요 수에 변화가 있는 경우
        if (args.changedLikeCount != -1) {
            allMovieReportAdapter.updateLikeState(updatedReportId = args.updatedReportId, isLiked = args.changedLikeState, likeCount = args.changedLikeCount)
        }
        if (args.isDeleted) {
            viewModel.handleEvent(AllMovieReportEvent.RefreshReport)
            binding.swiperefresh.isRefreshing = true
        }
        if (args.reportChanged) {
            viewModel.updateReport(args.updatedReportId)
            binding.swiperefresh.isRefreshing = true
        }
    }

    override fun handleEffect(effect: AllMovieReportEffect) {
        when (effect) {
            is AllMovieReportEffect.RegistLike -> {
                allMovieReportAdapter.updateLikeState(effect.reportId, true, effect.likeCount)
            }

            is AllMovieReportEffect.CancelLike -> {
                allMovieReportAdapter.updateLikeState(effect.reportId, false, effect.likeCount)
            }
            else -> {}
        }
    }

    override fun onBindLayout() {
        binding.layoutShimmer.startShimmer()

        // 영화 가져오기
        collectLatestStateFlow(viewModel.upcomingMovieList) {
            binding.movieRecyclerview.apply {
                movieInfoAdapter.setMovieInfoList(it)
            }
        }

        // 감상문 내용에 변경이 있는 경우
        collectLatestStateFlow(viewModel.updatedReportStateInfo) {
            adapter?.updateModifyReport(it.reportId, it.reportTitle, it.reportContent, it.posterUri)
        }

        collectLatestStateFlow(viewModel.pagingData) {
            binding.layoutShimmer.stopShimmer()
            binding.layoutShimmer.visibility = View.GONE
            allMovieReportAdapter.submitData(pagingData = it)
            binding.swiperefresh.isRefreshing = false
        }

        collectLatestStateFlow(allMovieReportAdapter.loadStateFlow) {
            when (it.refresh) {
                is LoadState.Loading -> {
                    binding.layoutShimmer.visibility = View.VISIBLE
                    binding.layoutShimmer.startShimmer()
                    binding.reportProgressBar.visibility = View.GONE
                    binding.movieRecyclerview.visibility = View.GONE
                }
                is LoadState.NotLoading -> {
                    binding.layoutShimmer.stopShimmer()
                    binding.layoutShimmer.visibility = View.GONE
                    binding.reportProgressBar.visibility = View.GONE
                    binding.movieRecyclerview.visibility = View.VISIBLE
                    binding.swiperefresh.isRefreshing = false
                }
                is LoadState.Error -> {
                    binding.layoutShimmer.stopShimmer()
                    binding.layoutShimmer.visibility = View.GONE
                    binding.reportProgressBar.visibility = View.GONE
                }
            }
        }

        // 로딩 추가
        // moviePosterAdapter의 loadStateFlow 속성에서 값을 수집한다.
        // 데이터의 현재 로드상태(로드 중, 성공적으로 로드되었는지, 오류가 발생했는지)를 내보낸다.
        // collectLatest : 흐름에서 방출된 최신값을 수집한다.
        // 가장 최근 상태만 중요한 UI 업데이트에서 유용하다.

        with(binding) {
            allMovieReportRecyclerview.adapter = allMovieReportAdapter
            movieRecyclerview.adapter = movieInfoAdapter
            // 새로 고침
            swiperefresh.setOnRefreshListener {
                viewModel.handleEvent(AllMovieReportEvent.RefreshReport)
                binding.layoutShimmer.visibility = View.VISIBLE
                binding.layoutShimmer.startShimmer()
                binding.movieRecyclerview.visibility = View.GONE
                swiperefresh.isRefreshing = true
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
                override fun onClick(report: ReportItem) {
                    navigateToBodyReport(report.reportId)
                }

                override fun onLikeClick(report: ReportItem) {
                    viewModel.handleEvent(AllMovieReportEvent.ClickLike(report.reportId))
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
        private var adapter: AllMovieReportAdapter? = null

        fun newInstance(): AllMovieReportFragment {
            val args = Bundle()
            val fragment = AllMovieReportFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onDetach() {
        super.onDetach()
        adapter = null
    }
}
