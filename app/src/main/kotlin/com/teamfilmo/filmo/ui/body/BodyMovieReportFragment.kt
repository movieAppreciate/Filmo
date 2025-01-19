package com.teamfilmo.filmo.ui.body

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class BodyMovieReportFragment :
    BaseFragment<FragmentBodyMovieReportBinding, BodyMovieReportViewModel, BodyMovieReportEffect, BodyMovieReportEvent>(
        FragmentBodyMovieReportBinding::inflate,
    ) {
    private var movieName: String = ""
    override val viewModel: BodyMovieReportViewModel by viewModels()
    private val navController by lazy { findNavController() }
    val args: BodyMovieReportFragmentArgs by navArgs()
    private lateinit var bottomSheet: ModalBottomSheet

    private val destinationChangedListener =
        NavController.OnDestinationChangedListener { controller, destination, arguments ->
            if (destination.id == R.id.bodyMovieReportFragment) {
                // Reply에서 Body로 돌아왔을 때 실행할 코드
                viewModel.handleEvent(BodyMovieReportEvent.ShowReport(args.reportId))
            }
        }

    override fun onBindLayout() {
        super.onBindLayout()

        navController.addOnDestinationChangedListener(destinationChangedListener)
        with(binding) {
            btnLike.setOnClickListener {
                viewModel.handleEvent(BodyMovieReportEvent.ClickLikeButton)
            }

            movieDetail.movieDetailShimmer.visibility = View.GONE

            // 뒤로 가기 버튼 클릭 시 이전 프래그먼트 보이도록(새 객체 x, 이전 상태 보존)
            btnBack.setOnClickListener {
                val action = BodyMovieReportFragmentDirections.actionBodyMovieReportFragmentToAllMovieReportFragment(args.reportId, isDeleted = false, isUpdated = true)
                navController.navigate(action)
            }

            /*
       유저 이름 클릭 시 감상문을  작성한 유저의 페이지로 이동
             */
            txtUserName.setOnClickListener {
                val userId = viewModel.getReportResponse.value.userId
                if (userId != null) {
                    // 탈퇴한 유저가 아니라면 유저 페이지로 이동
                    val action = BodyMovieReportFragmentDirections.navigatToUserPageFromBody(userId)
                    navController.navigate(action)
                } else {
                    //
                }
            }
            /*
      팔로잉 버튼 클릭 시
             */
            btnUserFollow.setOnClickListener {
                // 탈퇴한 사용자의 경우 팔로우 안되도록
                if (viewModel.getReportResponse.value.userId != null) {
                    viewModel.handleEvent(BodyMovieReportEvent.ClickFollow)
                } else {
                    Toast.makeText(context, "탈퇴한 사용자입니다", Toast.LENGTH_SHORT).show()
                }
            }
            /*
       미트볼 버튼 클릭 시
             */
            btnMeatBall.setOnClickListener {
                showMeatBallDialog()
            }

            movieDetail.readMore.setOnClickListener {
                movieDetail.readMore.visibility = View.GONE
                viewModel.handleEvent(BodyMovieReportEvent.ClickMoreButton)
                movieDetail.txtSummary.maxLines = 100
            }

            btnReply.setOnClickListener { navigateToReply(args.reportId) }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.movieContent.collect {
                    binding.movieDetail.txtSummary.text = it
                }
            }

            launch {
                viewModel.isMyPost.collect {
                    if (it) {
                        binding.btnUserFollow.visibility = View.INVISIBLE
                    } else {
                        binding.btnUserFollow.visibility = View.VISIBLE
                    }
                }
            }
            launch {
                viewModel.checkLikeResponse.collectLatest {
                    binding.btnLike.isSelected = it.isLike
                }
            }
            launch {
                viewModel.likeCount.collect {
                    binding.tvLikeCount.text = it.toString()
                }
            }
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
                    binding.apply {
                        txtUserName.text = it.nickname ?: "익명의 리뷰어"
                        tvMovieTitle.text = movieName
                        tvReportTitle.text = it.title
                        reportContent.text = it.content
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
                viewModel.movieDetailInfo.collect {
                    Timber.d("detailMovie :$it")
                    binding.tvMovieTitle.text = it.title
                    movieName = it.title.toString()
                    binding.movieDetail.apply {
                        txtRank.text = getMovieRankInfo(it.certification.toString())
                        txtMovieTitle.text = it.title
                        txtMovieEngTitle.text = it.originalTitle
                        txtReleaseDate.text = it.releaseDate
                        txtGenre.text =
                            it.genres?.joinToString(", ") {
                                it.name.toString()
                            } ?: ""
                        // 여기서 null이 아니라 아예 리스트가 빈 경우 first로 접근이 불가능해서 발생한 문제였다!
                        txtNation.text = if (it.productionCompanies?.isEmpty() == true) "" else it.productionCompanies?.first()?.origin_country
                        txtRationing.text = if (it.productionCompanies?.isEmpty() == true) "" else it.productionCompanies?.joinToString(", ") { it.name.toString() } ?: ""
                        txtRunningTime.text = it.runtime.toString()
                        txtSummary.text = it.overview.toString()

                        getImage("https://image.tmdb.org/t/p/original" + it.posterPath, movieImage)

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

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(
                viewLifecycleOwner,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        if (viewModel.reportId != null) {
                            val action = BodyMovieReportFragmentDirections.actionBodyMovieReportFragmentToAllMovieReportFragment(args.reportId, isDeleted = false, isUpdated = true)
                            navController.navigate(action)
                        }
                    }
                },
            )
    }

    private fun navigateToReply(reportId: String) {
        val action = BodyMovieReportFragmentDirections.navigateToReply(reportId)
        navController.navigate(action)
    }

    // 신고  다이얼로그
    private fun showComplaintDialog() {
        val dialog =
            context?.let {
                CustomDialog(
                    button2Text = "신고",
                    dialogMessage = "해당 감상문을 신고할까요?",
                )
            }
        dialog?.show(parentFragmentManager, "ComplaintDialog")
        dialog?.setButton2ClickListener(
            object : ItemClickListener {
                override fun onButton2Click() {
                    // todo : 감상문 신고 뷰모델 로직 호출
                    viewModel.handleEvent(BodyMovieReportEvent.SaveComplaint)
                }

                override fun onButton1Click() {
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
                    dialogMessage = "계정을 차단할까요?",
                )
            }

        dialog?.setButton2ClickListener(
            object : ItemClickListener {
                override fun onButton2Click() {
                    // todo : 감상문 차단 뷰모델 로직 호출
                    if (viewModel.getReportResponse.value.userId == null) Toast.makeText(context, "탈퇴한 사용자입니다", Toast.LENGTH_SHORT).show()
                    viewModel.handleEvent(BodyMovieReportEvent.SaveBlock)
                }

                override fun onButton1Click() {
                    dialog.dismiss()
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

        dialog.setButton2ClickListener(
            object : ItemClickListener {
                override fun onButton2Click() {
                    if (viewModel.getReportResponse.value.reportId != null) {
                        viewModel.handleEvent(BodyMovieReportEvent.DeleteReport(viewModel.getReportResponse.value.reportId!!))
                    }
                }

                override fun onButton1Click() {
                    dialog.dismiss()
                }
            },
        )
        dialog.show(parentFragmentManager, "DeleteDialog")
    }

    override fun handleEffect(effect: BodyMovieReportEffect) {
        when (effect) {
            is BodyMovieReportEffect.DeleteSuccess -> {
                Toast.makeText(context, "감상문을 삭제했어요!", Toast.LENGTH_SHORT).show()
                val action = BodyMovieReportFragmentDirections.deleteReportItem(args.reportId, isDeleted = true)
                navController.navigate(action)
            }
            is BodyMovieReportEffect.BlockSuccess -> {
                // 메인 화면으로 이동
                val action = BodyMovieReportFragmentDirections.deleteReportItem(args.reportId, isDeleted = true)
                navController.navigate(action)
            }
            is BodyMovieReportEffect.ComplaintSuccess -> {
                // 감상문 신고
                Toast.makeText(context, "감상문을 신고했어요!", Toast.LENGTH_SHORT).show()
                val action = BodyMovieReportFragmentDirections.deleteReportItem(args.reportId, isDeleted = true)
                navController.navigate(action)
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
        lifecycleScope.launch {
            viewModel.isMyPost.collect {
                if (it) {
                    bottomSheet =
                        ModalBottomSheet.newInstance(
                            listOf("수정하기", "삭제하기", "취소"),
                        )
                } else {
                    bottomSheet =
                        ModalBottomSheet.newInstance(
                            listOf("신고", "차단", "취소"),
                        )
                }
                return@collect
            }
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
                                viewModel.getReportResponse.value.reportId?.let {
                                    BodyMovieReportFragmentDirections.navigateToModifyReport(
                                        movieName,
                                        reportId = it,
                                        movieId =
                                            viewModel.getReportResponse.value.movieId
                                                .toString(),
                                    )
                                }
                            if (action != null) {
                                navController.navigate(action)
                            }
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

    override fun onDestroyView() {
        navController.removeOnDestinationChangedListener(destinationChangedListener)
        super.onDestroyView()
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
