package com.teamfilmo.filmo.ui.movie

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.teamfilmo.filmo.base.fragment.BaseFragment
import com.teamfilmo.filmo.databinding.FragmentMovieDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieDetailFragment :
    BaseFragment<FragmentMovieDetailBinding, MovieDetailViewModel, DetailMovieEffect, DetailMovieEvent>(
        FragmentMovieDetailBinding::inflate,
    ) {
    override val viewModel: MovieDetailViewModel by viewModels()
    private val navController by lazy { findNavController() }

    override fun onBindLayout() {
        binding.btnMovieDetailBack.visibility = View.VISIBLE

        super.onBindLayout()

        binding.itemMovieDetail.movieDetailShimmer.startShimmer()

        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.movieDetailInfo.collectLatest {
                    binding.itemMovieDetail.txtSummary.text = it.overview
                }
            }
            launch {
                viewModel.movieDetailInfo.collectLatest {
                    binding.itemMovieDetail.movieDetailShimmer.visibility = View.GONE

                    binding.itemMovieDetail.movieDetailShimmer.stopShimmer()
                    binding.itemMovieDetail.txtRank.text = getMovieRankInfo(it.certification.toString())
                    binding.itemMovieDetail.txtMovieTitle.text = it.title
                    binding.itemMovieDetail.txtMovieEngTitle.text = it.originalTitle
                    binding.itemMovieDetail.txtReleaseDate.text = it.releaseDate
                    binding.itemMovieDetail.txtSummary.text = it.overview
                    binding.itemMovieDetail.txtGenre.text =
                        it.genres?.joinToString(", ") {
                            it.name.toString()
                        }
                    binding.itemMovieDetail.txtNation.text = it.productionCompanies?.first()?.origin_country
                    binding.itemMovieDetail.txtRationing.text = it.productionCompanies?.joinToString(", ") { it.name.toString() }
                    binding.itemMovieDetail.txtRunningTime.text = it.runtime.toString()
                    Glide
                        .with(binding.root.context)
                        .asBitmap()
                        .load("https://image.tmdb.org/t/p/original" + it.posterPath)
                        .into(binding.itemMovieDetail.movieImage)
                }
            }

            binding.itemMovieDetail.readMore.setOnClickListener {
                binding.itemMovieDetail.txtSummary.text = viewModel.movieDetailInfo.value.overview
                binding.itemMovieDetail.txtSummary.maxLines = 100
                binding.itemMovieDetail.readMore.visibility = View.INVISIBLE
            }
            binding.btnMovieDetailBack.setOnClickListener {
                navController.popBackStack()
            }
        }
    }

    private fun getMovieRankInfo(certification: String): String {
        val rank =
            when (certification) {
                "ALL", "G", "NR" -> "전체 관람가"
                "12", "PG" -> "12세이상 관람가"
                "15", "PG-13" -> "15세이상 관람가"
                "18", "19", "R" -> "청소년 관람불가"
                else -> "정보 없음"
            }
        return rank
    }

    override fun handleEffect(effect: DetailMovieEffect) {
        when (effect) {
            is DetailMovieEffect.ShowDetailMovie -> {
                binding.itemMovieDetail.movieDetailShimmer.visibility = View.GONE
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
