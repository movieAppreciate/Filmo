package com.teamfilmo.filmo.ui.write.thumbnail

import android.os.Bundle
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
class ReportThumbnailFragment : BaseFragment<FragmentReportThumbnailBinding, ReportThumbnailViewModel, ReportThumbnailEffect, ReportThumbnailEvent>(
    FragmentReportThumbnailBinding::inflate,
) {
    override val viewModel: ReportThumbnailViewModel by viewModels()
    private val movieThumbnailAdapter by lazy {
        context?.let { MovieThumbnailAdapter(it) }
    }
    private val navController by lazy { findNavController() }
    private val args: ReportThumbnailFragmentArgs by navArgs()

    companion object {
        fun newInstance(
            movieName: String,
            movieId: String,
        ): ReportThumbnailFragment {
            return ReportThumbnailFragment().apply {
                arguments =
                    Bundle().apply {
                        putString("MOVIE_ID", movieId)
                    }
            }
        }
    }

    override fun onBindLayout() {
        val args: ReportThumbnailFragmentArgs by navArgs()

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
                    val action = ReportThumbnailFragmentDirections.navigateToWriteReport(uri.toString(), args.movieName, args.movieId)
                    navController.navigate(action)
                }
            },
        )

        binding.btnPoster.isSelected = true
        binding.btnBackground.isSelected = false
        binding.txtSelectedMovie.text = args.movieName

        lifecycleScope.launch {
            viewModel.handleEvent(ReportThumbnailEvent.SelectPoster(args.movieId.toString()))

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
