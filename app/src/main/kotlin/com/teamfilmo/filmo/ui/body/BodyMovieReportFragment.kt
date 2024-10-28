package com.teamfilmo.filmo.ui.body

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.teamfilmo.filmo.R
import com.teamfilmo.filmo.base.fragment.BaseFragment
import com.teamfilmo.filmo.databinding.FragmentBodyMovieReportBinding
import com.teamfilmo.filmo.ui.widget.ModalBottomSheet
import com.teamfilmo.filmo.ui.widget.OnButtonSelectedListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BodyMovieReportFragment :
    BaseFragment<FragmentBodyMovieReportBinding, BodyMovieReportViewModel, BodyMovieReportEffect, BodyMovieReportEvent>(
        FragmentBodyMovieReportBinding::inflate,
    ) {
    private var movieName: String = ""
    override val viewModel: BodyMovieReportViewModel by viewModels()
    private val navController by lazy { findNavController() }
    val args: BodyMovieReportFragmentArgs by navArgs()

    override fun onBindLayout() {
        binding.btnBack.setOnClickListener {
            navController.popBackStack()
        }
        /*
        유저 이름 클릭 시 감상문을  작성한 유저의 페이지로 이동
         */
        binding.txtUserName.setOnClickListener {
            val userId = viewModel.getReportResponse.value.userId
            val action = BodyMovieReportFragmentDirections.navigatToUserPageFromBody(userId)
            navController.navigate(action)
        }
        super.onBindLayout()

        binding.movieDetail.btnBack.visibility = View.GONE
        /*
         팔로잉 버튼 클릭 시
         */
        binding.btnUserFollow.setOnClickListener {
            viewModel.handleEvent(BodyMovieReportEvent.ClickFollow)
        }

        /*
        미트볼 버튼 클릭 시
         */
        binding.btnMeatBall.setOnClickListener {
            showMeatBallDialog()
        }

        with(binding.movieDetail) {
            readMore.setOnClickListener {
                readMore.visibility = View.GONE
                viewModel.handleEvent(BodyMovieReportEvent.ClickMoreButton)
                txtSummary.maxLines = 100
            }
        }

        viewModel.handleEvent(BodyMovieReportEvent.ShowReport(args.reportId))

        binding.btnReply.setOnClickListener {
            navigateToReply(args.reportId)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getReportResponse.collect {
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
                    getImage(it.imageUrl.toString(), binding.movieImage)
                    viewModel.handleEvent(BodyMovieReportEvent.ShowMovieInfo(it.movieId))
                }
            }
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

    private fun navigateToReply(reportId: String) {
        val action = BodyMovieReportFragmentDirections.navigateToReply(reportId)
        navController.navigate(action)
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

    private fun showReportDeleteDialog() {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setTitle("감상문 삭제")
        dialogBuilder.setMessage("감상문을 삭제하시겠습니까?")
        dialogBuilder.setPositiveButton("네!") { _, _ ->
            viewModel.handleEvent(BodyMovieReportEvent.DeleteReport(viewModel.getReportResponse.value.reportId))
            navController.navigate(R.id.allMovieReportFragment)
        }
        dialogBuilder.setNegativeButton("아니요!") { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    override fun handleEffect(effect: BodyMovieReportEffect) {
        when (effect) {
            is BodyMovieReportEffect.CancelFollow -> {
                // 팔로우 취소
                binding.btnUserFollow.isSelected = false
                Toast.makeText(context, "팔로우 취소", Toast.LENGTH_SHORT).show()
            }
            is BodyMovieReportEffect.SaveFollow -> {
                // 팔로우한 경우
                binding.btnUserFollow.isSelected = true
                Toast.makeText(context, "팔로우 완료!", Toast.LENGTH_SHORT).show()
            }

            is BodyMovieReportEffect.ShowMovieContent -> {
                lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.movieContent.collect {
                            binding.reportListView.text = it
                        }
                    }
                }
            }
            is BodyMovieReportEffect.ShowReport -> {
                lifecycleScope.launch {
                    launch {
                        viewModel.isMyPost.collect {
                            if (it) {
                                binding.btnMeatBall.visibility = View.VISIBLE
                            } else {
                                binding.btnMeatBall.visibility = View.GONE
                            }
                        }
                    }
                    launch {
                        viewModel.getReportResponse.collect {
                            binding.tvMovieTitle.text = movieName
                            binding.tvReportTitle.text = it.title
                            binding.tvLikeCount.text = it.likeCount.toString()
                            binding.tvReplyCount.text = it.replyCount.toString()
                            getImage(it.imageUrl.toString(), binding.movieImage)
                        }
                    }
                }
            }

            is BodyMovieReportEffect.ShowMovieInfo -> {
                lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.movieDetailInfo.collect {
                            binding.tvMovieTitle.text = it.title
                            movieName = it.title.toString()
                            binding.movieDetail.apply {
                                txtRank.text = getMovieRankInfo(it.certification)
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
                                    it.providers
                                        ?.rent
                                        ?.map {
                                            "https://image.tmdb.org/t/p/original" + it.logo_path
                                        }?.distinct()

                                val buyLogoPath =
                                    it.providers
                                        ?.buy
                                        ?.map {
                                            "https://image.tmdb.org/t/p/original" + it.logo_path
                                        }?.distinct()

                                val flatrateLogoPath =
                                    it.providers
                                        ?.flatrate
                                        ?.map {
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
        Glide
            .with(binding.root.context)
            .asBitmap()
            .load(imageUrl)
            .into(view as ImageView)
    }

    private fun showMeatBallDialog() {
        val bottomSheet =
            ModalBottomSheet.newInstance(
                listOf("수정하기", "삭제하기", "취소"),
            )

        bottomSheet.show(parentFragmentManager, ModalBottomSheet.TAG)
        bottomSheet.setListener(
            object : OnButtonSelectedListener {
                override fun onButtonSelected(text: String) {
                    when (text) {
                        "수정하기" -> {
                            Toast.makeText(context, "감상문을 수정합니다.", Toast.LENGTH_SHORT).show()
                            val action =
                                BodyMovieReportFragmentDirections.navigateToModifyReport(
                                    movieName,
                                    reportId = viewModel.getReportResponse.value.reportId,
                                    movieId =
                                        viewModel.getReportResponse.value.movieId
                                            .toString(),
                                )
                            navController.navigate(action)
                            bottomSheet.dismiss()
                        }

                        "삭제하기" -> {
                            showReportDeleteDialog()
                            bottomSheet.dismiss()
                        }

                        "취소" -> {
                            bottomSheet.dismiss()
                        }
                    }
                }
            },
        )
    }

    companion object {
        fun newInstance(): BodyMovieReportFragment {
            val args = Bundle()
            val fragment = BodyMovieReportFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
