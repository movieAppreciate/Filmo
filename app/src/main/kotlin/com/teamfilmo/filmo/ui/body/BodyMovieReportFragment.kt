package com.teamfilmo.filmo.ui.body

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
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
import com.teamfilmo.filmo.ui.widget.CustomDialog
import com.teamfilmo.filmo.ui.widget.ItemClickListener
import com.teamfilmo.filmo.ui.widget.ModalBottomSheet
import com.teamfilmo.filmo.ui.widget.OnButtonSelectedListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BodyMovieReportFragment :
    BaseFragment<FragmentBodyMovieReportBinding, BodyMovieReportViewModel, BodyMovieReportEffect, BodyMovieReportEvent>(
        FragmentBodyMovieReportBinding::inflate,
    ) {
    private var isMyPost: Boolean = false
    private var movieName: String = ""
    override val viewModel: BodyMovieReportViewModel by viewModels()
    private val navController by lazy { findNavController() }
    val args: BodyMovieReportFragmentArgs by navArgs()

    override fun onBindLayout() {
        super.onBindLayout()

        viewModel.handleEvent(BodyMovieReportEvent.ShowReport(args.reportId))

        with(binding) {
            // 뒤로 가기 버튼 클릭 시 이전 프래그먼트 보이도록(새 객체 x, 이전 상태 보존)
            btnBack.setOnClickListener { navController.popBackStack() }

            /*
       유저 이름 클릭 시 감상문을  작성한 유저의 페이지로 이동
             */
            txtUserName.setOnClickListener {
                val userId = viewModel.getReportResponse.value.userId
                val action = BodyMovieReportFragmentDirections.navigatToUserPageFromBody(userId)
                navController.navigate(action)
            }
            movieDetail.btnBack.visibility = View.GONE
            /*
      팔로잉 버튼 클릭 시
             */
            btnUserFollow.setOnClickListener {
                viewModel.handleEvent(BodyMovieReportEvent.ClickFollow)
            }
            /*
       미트볼 버튼 클릭 시
             */
            btnMeatBall.setOnClickListener {
                showMeatBallDialog()
            }
            movieDetail.apply {
                readMore.setOnClickListener {
                    readMore.visibility = View.GONE
                    viewModel.handleEvent(BodyMovieReportEvent.ClickMoreButton)
                    txtSummary.maxLines = 100
                }
            }
            btnReply.setOnClickListener { navigateToReply(args.reportId) }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.saveBlockResponse.collect {
                    if (it.blockId != null) {
                        Toast.makeText(context, "감상문을 차단했어요!", Toast.LENGTH_SHORT).show()
                        navController.navigate(R.id.allMovieReportFragment)
                    }
                }
            }
            launch {
                viewModel.getReportResponse.collect {
                    viewModel.handleEvent(BodyMovieReportEvent.ShowMovieInfo(it.movieId))
                    binding.apply {
                        txtUserName.text = "뚱"
                        tvMovieTitle.text = movieName
                        tvReportTitle.text = it.title
                        reportListView.text = it.content
                        tvLikeCount.text = it.likeCount.toString()
                        tvReplyCount.text = if (it.replyCount > 100) "100+" else it.replyCount.toString()
                    }
                    getImage(it.imageUrl.toString(), binding.movieImage)
                }
            }
            launch {
                viewModel.checkIsFollowResponse.collect {
                    binding.btnUserFollow.isSelected = it.isFollowing
                }
            }
            launch {
                viewModel.isMyPost.collect {
                    if (it) {
                        binding.btnUserFollow.visibility = View.GONE
                    } else {
                        binding.btnUserFollow.visibility = View.VISIBLE
                    }
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

    // 신고  다이얼로그
    private fun showComplaintDialog() {
        val dialog =
            context?.let {
                CustomDialog(
                    button2Text = "신고",
                    dialogMessage = "감상문을 신고할까요?",
                )
            }
        dialog?.show(parentFragmentManager, "ComplaintDialog")
        dialog?.setItemClickListener(
            object : ItemClickListener {
                override fun onClick() {
                    // todo : 감상문 신고 뷰모델 로직 호출
                    viewModel.handleEvent(BodyMovieReportEvent.SaveComplaint)
                }
            },
        )
    }

    // 차단  다이얼로그
    private fun showBlockDialog() {
        val dialog =
            context?.let {
                CustomDialog(
                    button2Text = "차단",
                    dialogMessage = "감상문을 차단할까요?",
                )
            }

        dialog?.setItemClickListener(
            object : ItemClickListener {
                override fun onClick() {
                    // todo : 감상문 차단 뷰모델 로직 호출
                    viewModel.handleEvent(BodyMovieReportEvent.SaveBlock)
                }
            },
        )
        dialog?.show(parentFragmentManager, "BlockDialog")
    }

    // 삭제 다이얼로그
    fun showDeleteDialog() {
        val dialog =
            CustomDialog(
                button2Text = "삭제",
                dialogMessage = "감상문을 삭제할까요?",
            )

        dialog.setItemClickListener(
            object : ItemClickListener {
                override fun onClick() {
                    viewModel.handleEvent(BodyMovieReportEvent.DeleteReport(viewModel.getReportResponse.value.reportId))
                    navController.navigate(R.id.allMovieReportFragment)
                    Toast.makeText(context, "감상문을 삭제했어요!", Toast.LENGTH_SHORT).show()
                }
            },
        )
        dialog.show(parentFragmentManager, "DeleteDialog")
    }

    override fun handleEffect(effect: BodyMovieReportEffect) {
        when (effect) {
            is BodyMovieReportEffect.BlockSuccess -> {}
            // todo : 왜 여기에 UI 처리를 하면 안뜰까?
            is BodyMovieReportEffect.ComplaintSuccess -> {
                // 감상문 신고
                lifecycleScope.launch {
                    viewModel.registComplaintResponse.collect {
                        Toast.makeText(context, "감상문을 신고했어요!", Toast.LENGTH_SHORT).show()
                        navController.navigate(R.id.allMovieReportFragment)
                    }
                }
            }
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
                            isMyPost = it
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

    // 본인 감상문인지 여부에 따라서 다르게 해야함

    private fun showMeatBallDialog() {
        val bottomSheet =
            if (isMyPost) {
                ModalBottomSheet.newInstance(
                    listOf("수정하기", "삭제하기", "취소"),
                )
            } else {
                ModalBottomSheet.newInstance(
                    listOf("신고", "차단", "취소"),
                )
            }

        bottomSheet.show(parentFragmentManager, ModalBottomSheet.TAG)
        bottomSheet.setListener(
            object : OnButtonSelectedListener {
                override fun onButtonSelected(text: String) {
                    when (text) {
                        // Todo : 신고 차단 기능 추가 필요
                        "신고" -> {
                            bottomSheet.dismiss()
                            showComplaintDialog()
                        }
                        "차단" -> {
                            bottomSheet.dismiss()
                            showBlockDialog()
                        }

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
                            showDeleteDialog()
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
