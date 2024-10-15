package com.teamfilmo.filmo.ui.movie

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.teamfilmo.filmo.base.fragment.BaseFragment
import com.teamfilmo.filmo.databinding.FragmentMovieDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MovieDetailFragment :
    BaseFragment<FragmentMovieDetailBinding, MovieDetailViewModel, DetailMovieEffect, DetailMovieEvent>(
        FragmentMovieDetailBinding::inflate,
    ) {
    override val viewModel: MovieDetailViewModel by viewModels()
    private val navController by lazy { findNavController() }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val movieId = arguments?.getInt("MOVIE_ID")
        Timber.d("받은 영화 id : $movieId")
        lifecycleScope.launch {
            if (movieId != null) {
                viewModel.searchMovieDetail(movieId)
            }
        }

        requireActivity().onBackPressedDispatcher
            .addCallback(
                viewLifecycleOwner,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        if (movieId != null) {
                            navController.popBackStack()
                        }
                    }
                },
            )
    }

    override fun handleEffect(effect: DetailMovieEffect) {
        when (effect) {
            is DetailMovieEffect.ShowDetailMovie -> {
                lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.movieDetailInfo.value.apply {
                            binding.txtMovieTitle.text = this.title
                            binding.txtMovieEngTitle.text = this.original_title
                            binding.txtReleaseDate.text = this.release_date
                            binding.txtSummary.text = this.overview
                            binding.txtGenre.text =
                                this.genres?.joinToString(", ") {
                                    it.name.toString()
                                }
                            binding.txtNation.text = this.production_companies?.first()?.origin_country
                            binding.txtRationing.text = this.production_companies?.joinToString(", ") { it.name.toString() }
                            binding.txtRunningTime.text = this.runtime.toString()
                            Glide.with(binding.root.context)
                                .asBitmap()
                                .load("https://image.tmdb.org/t/p/original" + this.poster_path)
                                .into(binding.movieImage)
                        }
                        binding.readMore.setOnClickListener {
                            binding.readMore.visibility = View.GONE
                            lifecycleScope.launch {
                                viewModel.handleEvent(DetailMovieEvent.ClickMoreButton)
                                binding.txtSummary.maxLines = 100
                                repeatOnLifecycle(Lifecycle.State.STARTED) {
                                    viewModel.movieDetailInfo.collect {
                                        binding.txtSummary.text = it.overview
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        fun newInstance(
            movieID: Int,
        ): MovieDetailFragment {
            val fragment =
                MovieDetailFragment().apply {
                    arguments =
                        Bundle().apply {
                            putInt("MOVIE_ID", movieID)
                        }
                }
            return fragment
        }
    }
}
