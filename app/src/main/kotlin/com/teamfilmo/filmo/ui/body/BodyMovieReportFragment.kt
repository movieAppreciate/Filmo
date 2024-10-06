package com.teamfilmo.filmo.ui.body

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.teamfilmo.filmo.base.fragment.BaseFragment
import com.teamfilmo.filmo.databinding.FragmentBodyMovieReportBinding
import com.teamfilmo.filmo.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class BodyMovieReportFragment : BaseFragment<FragmentBodyMovieReportBinding, BodyMovieReportViewModel, BodyMovieReportEffect, BodyMovieReportEvent>(
    FragmentBodyMovieReportBinding::inflate,
) {
    override val viewModel: BodyMovieReportViewModel by viewModels()

    override fun onBindLayout() {
        super.onBindLayout()

        val reportId = arguments?.getString("REPORT_ID") ?: ""

        viewModel.handleEvent(BodyMovieReportEvent.ShowReport(reportId))

        binding.btnReply.setOnClickListener {
            Timber.d("reply fragment에 전달한 ReportId : $reportId")
            (activity as MainActivity).navigateToReplyFragment(reportId)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.report.collect {
                    binding.tvMovieTitle.text = it.movieId.toString()
                    binding.tvReportTitle.text = it.title
                    binding.reportListView.text = it.content
                    binding.tvLikeCount.text = it.likeCount.toString()
                    binding.tvReplyCount.text =
                        if (it.replyCount > 100) {
                            (
                                {
                                    binding.tvReplyCount.text = "100+"
                                }
                            ).toString()
                        } else {
                            it.replyCount.toString()
                        }
                    viewModel.handleEvent(BodyMovieReportEvent.ShowMovieInfo(it.movieId))
                    it.imageUrl?.let { it1 -> getImage(it1, binding.movieImage) }
                }
            }
        }

        requireActivity().onBackPressedDispatcher
            .addCallback(
                viewLifecycleOwner,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        (activity as MainActivity).navigateToReportFragment()
                    }
                },
            )
    }

    override fun handleEffect(effect: BodyMovieReportEffect) {
        when (effect) {
            is BodyMovieReportEffect.ShowReport -> {
                lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.report.collect {
                            binding.tvMovieTitle.text = it.movieId.toString()
                            binding.tvReportTitle.text = it.title
                            binding.tvLikeCount.text = it.likeCount.toString()
                            binding.tvReplyCount.text = it.replyCount.toString()
                            it.imageUrl?.let { it1 -> getImage(it1, binding.movieImage) }
                        }
                    }
                }
            }
            is BodyMovieReportEffect.ShowMovieInfo -> {
                lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.movieDetailInfo.collect {
                            binding.tvMovieTitle.text = it.title
                            binding.movieDetail.apply {
                                txtMovieTitle.text = it.title
                                txtMovieEngTitle.text = it.original_title
                                txtReleaseDate.text = it.release_date
                                txtGenre.text =
                                    it.genres?.joinToString(", ") {
                                        it.name.toString()
                                    }
                                txtNation.text = it.production_companies?.first()?.origin_country
                                txtRationing.text = it.production_companies?.joinToString(", ") { it.name.toString() }
                                txtRunningTime.text = it.runtime.toString()
                                txtSummary.text = it.overview.toString()

                                getImage("https://image.tmdb.org/t/p/original" + it.poster_path, movieImage)

                                val rentLogoPath =
                                    it.providers?.rent?.map {
                                        "https://image.tmdb.org/t/p/original" + it.logo_path
                                    }?.distinct()

                                val buyLogoPath =
                                    it.providers?.buy?.map {
                                        "https://image.tmdb.org/t/p/original" + it.logo_path
                                    }?.distinct()

                                val flatrateLogoPath =
                                    it.providers?.flatrate?.map {
                                        "https://image.tmdb.org/t/p/original" + it.logo_path
                                    }?.distinct()

                                val combinedLogoPaths =
                                    listOfNotNull(
                                        rentLogoPath,
                                        buyLogoPath,
                                        flatrateLogoPath,
                                    ).flatten().distinct()

                                combinedLogoPaths.forEachIndexed { index, logoPath ->
                                    when (index) {
                                        0 -> getImage(logoPath, binding.imagePlatform1)
                                        1 -> getImage(logoPath, binding.imagePlatform2)
                                        2 -> getImage(logoPath, binding.imagePlatform3)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getImage(
        imageUrl: String,
        view: View,
    ) {
        Glide.with(binding.root.context)
            .asBitmap()
            .load(imageUrl)
            .into(view as ImageView)
    }

    companion object {
        fun newInstance(
            reportId: String,
        ): BodyMovieReportFragment {
            val fragment =
                BodyMovieReportFragment().apply {
                    arguments =
                        Bundle().apply {
                            putString("REPORT_ID", reportId)
                        }
                }
            return fragment
        }
    }
}