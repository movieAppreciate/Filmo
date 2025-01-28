package com.teamfilmo.filmo.ui.write.thumbnail

import android.os.Bundle
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.teamfilmo.filmo.base.fragment.BaseFragment
import com.teamfilmo.filmo.databinding.FragmentReportThumbnailBinding
import com.teamfilmo.filmo.ui.write.adapter.MovieThumbnailAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReportThumbnailFragment :
    BaseFragment<FragmentReportThumbnailBinding, ReportThumbnailViewModel, ReportThumbnailEffect, ReportThumbnailEvent>(
        FragmentReportThumbnailBinding::inflate,
    ) {
    override val viewModel: ReportThumbnailViewModel by viewModels()
    private val movieThumbnailAdapter by lazy {
        context?.let { MovieThumbnailAdapter(it) }
    }
    val args: ReportThumbnailFragmentArgs by navArgs()
    private val navController by lazy { findNavController() }

    companion object {
        fun newInstance(): ReportThumbnailFragment {
            val args = Bundle()
            val fragment = ReportThumbnailFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onBindLayout() {
        // 기본 스팬 적용
        val span = 3
        binding.movieImageGridView.layoutManager = GridLayoutManager(requireContext(), span)
        binding.movieImageGridView.adapter = movieThumbnailAdapter
        binding.movieImageGridView.layoutManager = GridLayoutManager(requireContext(), 3)
        movieThumbnailAdapter?.setViewType(0)

        movieThumbnailAdapter?.setOnItemClickListener(
            object : MovieThumbnailAdapter.OnItemClickListener {
                override fun onItemClick(
                    position: Int,
                    uri: String?,
                ) {
                    // 전달해야할 데이터는 safe args로 할 수 없으므로 번들로 전달한다.
                    val result =
                        Bundle().apply {
                            putString("uri", uri.toString())
                        }
                    setFragmentResult("requestKey", result)
                    // navigate() 로 이동 시에는 기존 프래그먼트가 아니라 새로 생성된 프래그먼트로 이동되어 작성한 감상문 내용이 사라진다.
                    // 따라서 popbackStack을 해준다.

                    navController.popBackStack()
                }
            },
        )

        binding.btnPoster.isSelected = true
        binding.btnBackground.isSelected = false
        binding.txtSelectedMovie.text = args.movieName

        lifecycleScope.launch {
            viewModel.handleEvent(ReportThumbnailEvent.SelectPoster(args.movieId))

            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.moviePosterList.collect {
                    movieThumbnailAdapter?.setPosterUriList(it)
                }
            }
        }

        binding.btnPoster.setOnClickListener {
            binding.movieImageGridView.layoutManager = GridLayoutManager(requireContext(), 3)
            movieThumbnailAdapter?.setViewType(0)
            binding.btnPoster.isSelected = true
            binding.btnBackground.isSelected = false

            lifecycleScope.launch {
                viewModel.handleEvent(ReportThumbnailEvent.SelectPoster(args.movieId.toString()))

                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.moviePosterList.collect {
                        movieThumbnailAdapter?.setPosterUriList(it)
                    }
                }
            }
        }

        binding.btnBackground.setOnClickListener {
            val layoutManager = GridLayoutManager(requireContext(), 2)
            binding.movieImageGridView.layoutManager = layoutManager
            movieThumbnailAdapter?.setViewType(2)
            binding.btnPoster.isSelected = false
            binding.btnBackground.isSelected = true

            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.handleEvent(ReportThumbnailEvent.SelectBackground(args.movieId.toString()))
                    viewModel.movieBackdropList.collect {
                        movieThumbnailAdapter?.setPosterUriList(it)
                    }
                }
            }
        }

        binding.btnBack.setOnClickListener {
            navController.popBackStack()
        }
    }
}
