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
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.teamfilmo.filmo.base.fragment.BaseFragment
import com.teamfilmo.filmo.databinding.FragmentMovieDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieDetailFragment :
    BaseFragment<FragmentMovieDetailBinding, MovieDetailViewModel, DetailMovieEffect, DetailMovieEvent>(
        FragmentMovieDetailBinding::inflate,
    ) {
    override val viewModel: MovieDetailViewModel by viewModels()
    private val navController by lazy { findNavController() }

    override fun onBindLayout() {
        super.onBindLayout()
        binding.btnBack.setOnClickListener {
            navController.popBackStack()
        }
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        binding.movieDetailShimmer.startShimmer()

        val args: MovieDetailFragmentArgs by navArgs()
        lifecycleScope.launch {
            viewModel.searchMovieDetail(args.movieId)
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(
                viewLifecycleOwner,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        navController.popBackStack()
                    }
                },
            )
    }

    private fun getMovieRankInfo(certification: String): String {
        val rank =
            when (certification) {
                "ALL", "G", "NR" -> "전체 관람가"
                "12" -> "12세이상 관람가"
                "15" -> "15세이상 관람가"
                "18" -> "18세이상 관람가"
                "PG" -> "10세 이상 관람 불가"
                "19" -> "청소년 관람불가"
                "PG-13" -> "14세 이상 관람가"
                "R" -> "17세 미만 청소년은 보호자와 함께 관람"
                else -> "정보없음"
            }
        return rank
    }

    override fun handleEffect(effect: DetailMovieEffect) {
        when (effect) {
            is DetailMovieEffect.ShowDetailMovie -> {
                lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.movieDetailInfo.value.apply {
                            binding.movieDetailShimmer.stopShimmer()
                            binding.movieDetailShimmer.visibility = View.GONE
                            binding.txtRank.text = getMovieRankInfo(this.certification)
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
                            Glide
                                .with(binding.root.context)
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
        fun newInstance(): MovieDetailFragment {
            val args = Bundle()
            val fragment = MovieDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
