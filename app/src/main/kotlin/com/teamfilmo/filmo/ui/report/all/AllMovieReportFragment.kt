package com.teamfilmo.filmo.ui.report.all

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.teamfilmo.filmo.base.fragment.BaseFragment
import com.teamfilmo.filmo.data.remote.model.report.all.ReportItem
import com.teamfilmo.filmo.databinding.FragmentAllMovieReportBinding
import com.teamfilmo.filmo.ui.report.adapter.AllMovieReportAdapter
import com.teamfilmo.filmo.ui.report.adapter.MovieInfoAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllMovieReportFragment :
    BaseFragment<FragmentAllMovieReportBinding, AllMovieReportViewModel, AllMovieReportEffect, AllMovieReportEvent>(
        FragmentAllMovieReportBinding::inflate,
    ) {
    override val viewModel: AllMovieReportViewModel by viewModels()
    private val navController by lazy { findNavController() }
    private val allMovieReportAdapter by lazy {
        AllMovieReportAdapter()
    }
    val movieInfoAdapter by lazy {
        MovieInfoAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        lifecycleScope.launch {
            viewModel.handleEvent(AllMovieReportEvent.LoadReport)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
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
            else -> {}
        }
    }

    override fun onBindLayout() {
        binding.layoutShimmer.startShimmer()

        // todo : 뒤로 가기 시 앱 종료 확인 로직 추가
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.pagingData.collectLatest {
                binding.layoutShimmer.stopShimmer()
                binding.layoutShimmer.visibility = View.GONE
                allMovieReportAdapter.submitData(pagingData = it)
            }
        }

        // 로딩 추가
        // moviePosterAdapter의 loadStateFlow 속성에서 값을 수집한다.
        // 데이터의 현재 로드상태(로드 중, 성공적으로 로드되었는지, 오류가 발생했는지)를 내보낸다.
        // collectLatest : 흐름에서 방출된 최신값을 수집한다.
        // 가장 최근 상태만 중요한 UI 업데이트에서 유용하다.
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                allMovieReportAdapter.loadStateFlow.collectLatest {
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
                        }
                        is LoadState.Error -> {
                            binding.layoutShimmer.stopShimmer()
                            binding.layoutShimmer.visibility = View.GONE
                            binding.reportProgressBar.visibility = View.GONE
                        }
                    }
                }
            }
        }

        childFragmentManager.commit {
            setReorderingAllowed(true)

            with(binding) {
                allMovieReportRecyclerview.adapter = allMovieReportAdapter
                movieRecyclerview.adapter = movieInfoAdapter
                // 새로고침
                swiperefresh.setOnRefreshListener {
                    viewModel.handleEvent(AllMovieReportEvent.RefreshReport)
                    swiperefresh.isRefreshing = false
                }
            }

            lifecycleScope.launch {
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
        fun newInstance(): AllMovieReportFragment {
            val args = Bundle()
            val fragment = AllMovieReportFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
