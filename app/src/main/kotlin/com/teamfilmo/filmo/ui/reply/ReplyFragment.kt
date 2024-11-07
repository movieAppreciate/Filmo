package com.teamfilmo.filmo.ui.reply

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.teamfilmo.filmo.R
import com.teamfilmo.filmo.base.fragment.BaseFragment
import com.teamfilmo.filmo.databinding.FragmentReplyBinding
import com.teamfilmo.filmo.ui.reply.adapter.ReplyItemClick
import com.teamfilmo.filmo.ui.reply.adapter.ReplyRVAdapter
import com.teamfilmo.filmo.ui.widget.CustomDialog
import com.teamfilmo.filmo.ui.widget.ItemClickListener
import com.teamfilmo.filmo.ui.widget.ModalBottomSheet
import com.teamfilmo.filmo.ui.widget.OnButtonSelectedListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReplyFragment :
    BaseFragment<FragmentReplyBinding, ReplyViewModel, ReplyEffect, ReplyEvent>(
        FragmentReplyBinding::inflate,
    ) {
    private var isReplyingToComment = false
    private var upReplyId = ""

    override val viewModel: ReplyViewModel by viewModels()

    val adapter by lazy {
        ReplyRVAdapter()
    }
    private val navController by lazy { findNavController() }

    val args: ReplyFragmentArgs by navArgs()

    // 차단 다이얼로그
    fun showBlockDialog(
        replyId: String,
    ) {
        val dialog =
            context?.let {
                CustomDialog(
                    button2Text = "차단",
                    dialogMessage = resources.getString(R.string.txt_block_reply),
                )
            }

        dialog?.setItemClickListener(
            object : ItemClickListener {
                override fun onClick() {
                    viewModel.handleEvent(ReplyEvent.SaveBlock(replyId))
                }
            },
        )
        dialog?.show(activity?.supportFragmentManager!!, "BlockDialog")
    }

    // 신고 다이얼로그
    fun showComplaintDialog(
        replyId: String,
    ) {
        val dialog =
            context?.let {
                CustomDialog(
                    button2Text = "신고",
                    dialogMessage = resources.getString(R.string.txt_complaint_reply),
                )
            }

        dialog?.setItemClickListener(
            object : ItemClickListener {
                override fun onClick() {
                    viewModel.handleEvent(ReplyEvent.SaveComplaint(replyId))
                }
            },
        )
        dialog?.show(activity?.supportFragmentManager!!, "ConstraintDialog")
    }

    fun showDeleteDialog(
        reportId: String,
        replyId: String,
        position: Int,
    ) {
        val dialog =
            context?.let {
                CustomDialog(
                    button2Text = "삭제",
                    dialogMessage = resources.getString(R.string.txt_delete_reply),
                )
            }
        dialog?.setItemClickListener(
            object : ItemClickListener {
                override fun onClick() {
                    viewModel.handleEvent(ReplyEvent.DeleteReply(replyId, reportId))
                    adapter.removeReplyItem(position)
                    Toast.makeText(context, "댓글을 삭제했어요!", Toast.LENGTH_SHORT).show()
                }
            },
        )
        // 다이얼로그가 띄워져 있는 동안 배경 클릭 막기
        dialog?.isCancelable = false
        dialog?.show(activity?.supportFragmentManager!!, "CustomDialog")
    }

    fun showDeleteSubReplyDialog(
        reportId: String,
        replyId: String,
        position: Int,
    ) {
        val dialog =
            context?.let {
                CustomDialog(
                    button2Text = "삭제",
                    dialogMessage = resources.getString(R.string.txt_delete_sub_reply),
                )
            }
        dialog?.setItemClickListener(
            object : ItemClickListener {
                override fun onClick() {
                    viewModel.handleEvent(ReplyEvent.DeleteSubReply(replyId, reportId))
                    adapter.removeReplyItem(position)
                    Toast.makeText(context, "답글을 삭제했어요!", Toast.LENGTH_SHORT).show()
                }
            },
        )
        dialog?.show(activity?.supportFragmentManager!!, "CustomDialog")
    }

    override fun onBindLayout() {
        // 댓글, 답글 전환
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (isReplyingToComment) {
                        isReplyingToComment = false
                        binding.editReply.hint = "댓글 달기"
                    } else {
                        parentFragmentManager.popBackStack()
                    }
                }
            },
        )
        val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        binding.recyclerView.adapter = adapter

        binding.btnBack.setOnClickListener {
            if (isReplyingToComment) {
                isReplyingToComment = false
                binding.editReply.hint = "댓글 달기"
            } else {
                navController.popBackStack()
            }
        }

        adapter.itemClick =
            object : ReplyItemClick {
                override fun onLikeClick(position: Int) {
                    viewLifecycleOwner.lifecycleScope.launch {
                        val replyId = adapter.replyList[position].replyId
                        viewModel.handleEvent(ReplyEvent.ClickLike(replyId))
                    }
                }

                override fun onReplyClick(position: Int) {
                    lifecycleScope.launch {
                        isReplyingToComment = true
                        repeatOnLifecycle(Lifecycle.State.STARTED) {
                            viewModel.replyListStateFlow.collect {
                                val item = it[position]
                                upReplyId = item.replyId
                                binding.editReply.requestFocus()
                                inputMethodManager.showSoftInput(binding.editReply, InputMethodManager.SHOW_FORCED)
                                binding.editReply.hint = "${item.nickname}에게 답글 작성하기"
                            }
                        }
                    }
                }

                // 본인이 작성한 댓글인지 아닌지에 따라서 미트볼 기능 분류
                override fun onMeatBallClick(
                    isMyReply: Boolean,
                    position: Int,
                ) {
                    val bottomSheet =
                        if (isMyReply) {
                            ModalBottomSheet.newInstance(
                                listOf("삭제하기", "취소"),
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
                                val replyId = adapter.replyList[position].replyId
                                when (text) {
                                    "신고" -> {
                                        showComplaintDialog(replyId)
                                        bottomSheet.dismiss()
                                    }
                                    "차단" -> {
                                        showBlockDialog(replyId)
                                        bottomSheet.dismiss()
                                    }
                                    "삭제하기" -> {
                                        showDeleteDialog(args.reportId, replyId, position)
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

                override fun onShowBottomSheet(
                    replyId: String,
                    position: Int,
                ) {
                    val bottomSheet =
                        ModalBottomSheet.newInstance(
                            listOf("삭제하기", "취소"),
                        )

                    bottomSheet.show(parentFragmentManager, ModalBottomSheet.TAG)
                    bottomSheet.setListener(
                        object : OnButtonSelectedListener {
                            override fun onButtonSelected(text: String) {
                                when (text) {
                                    "삭제하기" -> {
                                        showDeleteSubReplyDialog(args.reportId, replyId, position)
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
            }

        binding.editReply.setOnClickListener {
            binding.btnRegistReply.setImageResource(R.drawable.btn_save_reply)
        }
        binding.recyclerView.setOnClickListener {
            binding.editReply.hint = "댓글달기"
        }
        binding.btnRegistReply.setOnClickListener {
            if (isReplyingToComment) {
                viewModel.handleEvent(ReplyEvent.SaveSubReply(upReplyId, args.reportId, binding.editReply.text.toString()))
                Toast.makeText(context, "답글이 등록되었어요!", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.handleEvent(ReplyEvent.SaveReply(null, args.reportId, binding.editReply.text.toString()))
                Toast.makeText(context, "댓글이 등록되었어요!", Toast.LENGTH_SHORT).show()
            }
            binding.editReply.clearAnimation()
            binding.editReply.text.clear()
        }

        lifecycleScope.launch {
            /*
            댓글 차단
             */
            launch {
                viewModel.saveReplyBlockResponse.collect {
                    Toast.makeText(context, "댓글을 차단했어요!", Toast.LENGTH_SHORT).show()
                    // todo : 차단 시 UI 반영 사항은 ?
                }
            }
            /*
            댓글 신고
             */
            launch {
                viewModel.saveReplyComplaintResponse.collect {
                    Toast.makeText(context, "댓글을 신고했어요!", Toast.LENGTH_SHORT).show()
                    // todo : 차단 시 UI 반영 사항은 ?
                }
            }
            /*
         전체 댓글 가져오기
             */
            viewModel.getReply(args.reportId)

            launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.userInfo.collect {
                        it.let {
                            adapter.setUserId(it.userId)
                        }
                    }
                }
            }

            launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    // 처음에 댓글 불러올 때 & 댓글 삭제 시에도 호출된다.
                    viewModel.replyListStateFlow.collect {
                        if (it.isNotEmpty()) {
                            adapter.setReplyList(it)
                            binding.txtReplyCount.text = it.size.toString()
                        }
                    }
                }
            }
        }
    }

    override fun handleEffect(effect: ReplyEffect) {
        when (effect) {
            is ReplyEffect.SaveLike -> {
                adapter.updateLikeState(viewModel.replyListStateFlow.value)
            }
            is ReplyEffect.CancelLike -> {
                adapter.updateLikeState(viewModel.replyListStateFlow.value)
            }
            is ReplyEffect.ToggleLike -> {
                lifecycleScope.launch {
                    viewModel.likeCount.collect {
                        adapter.updateLikeState(viewModel.replyListStateFlow.value)
                    }
                }
            }

            is ReplyEffect.ScrollToTop -> {
                binding.recyclerView.scrollToPosition(viewModel.replyListStateFlow.value.size - 1)
            }
            is ReplyEffect.DeleteReply -> {
                adapter.removeReplyItem(effect.position)
            }
            is ReplyEffect.DeleteSubReply -> {
                lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.replyListStateFlow.collect {
                            adapter.setReplyList(it)
                        }
                    }
                }
            }
        }
    }

    companion object {
        fun newInstance(): ReplyFragment {
            val args = Bundle()
            val fragment = ReplyFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
