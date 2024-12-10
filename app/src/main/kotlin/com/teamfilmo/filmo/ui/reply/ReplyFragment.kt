package com.teamfilmo.filmo.ui.reply

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
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
import com.teamfilmo.filmo.ui.reply.adapter.ReplyRVAdapter
import com.teamfilmo.filmo.ui.widget.CustomDialog
import com.teamfilmo.filmo.ui.widget.ItemClickListener
import com.teamfilmo.filmo.ui.widget.ModalBottomSheet
import com.teamfilmo.filmo.ui.widget.OnButtonSelectedListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

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

    private fun hideKeyboard() {
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            binding.editReply.windowToken,
            0,
        )
    }

    private fun setupKeyboardDismiss() {
        binding.recyclerView.setOnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                if (binding.editReply.hasFocus()) {
                    val outRect = Rect()
                    binding.editReply.getGlobalVisibleRect(outRect)
                    if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                        binding.editReply.clearFocus()
                        hideKeyboard()
                    }
                }
            }
            false
        }
    }

    override fun onBindLayout() {
        // 전체 댓글 가져오기
        viewModel.getReply(args.reportId)
        setupKeyboardDismiss()

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
            object : ReplyInteractionListener {
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

                override fun onReplyLikeClick(replyId: String) {
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.handleEvent(ReplyEvent.ClickLike(replyId))
                    }
                }

                override fun onReplyMoreClick(
                    isMyReply: Boolean,
                    replyId: String,
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
                                        showDeleteDialog(args.reportId, replyId)
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

                override fun onSubReplyLikeClick(
                    parentReplyId: String,
                    subReplyId: String,
                ) {
                    Timber.d("답글 좋아요 뚜웅 ")
                    viewModel.handleEvent(ReplyEvent.ClickSubReplyLike(subReplyId))
                }

                override fun onSubReplyMoreClick(
                    isMyReply: Boolean,
                    parentReplyId: String,
                    subReplyId: String,
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
                                when (text) {
                                    "신고" -> {
                                        showComplaintDialog(subReplyId)
                                        bottomSheet.dismiss()
                                    }
                                    "차단" -> {
                                        showBlockDialog(subReplyId)
                                        bottomSheet.dismiss()
                                    }
                                    "삭제하기" -> {
                                        showDeleteDialog(args.reportId, subReplyId)
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

        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.replyListStateFlow.collect {
                        adapter.setReplyList(it)
                    }
                }
            }
            launch {
                viewModel.likeCount.collect {
                    adapter.updateLikeState(viewModel.replyListStateFlow.value)
                }
            }
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
                repeatOnLifecycle(Lifecycle.State.STARTED) {
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
            is ReplyEffect.CancelLikeSubReply -> {
//                viewModel.getReply(args.reportId)
                adapter.replyList.find { it.replyId == effect.upReplyId }?.let {
                    val position = adapter.replyList.indexOf(it)
                    val holder =
                        binding.recyclerView
                            .findViewHolderForAdapterPosition(position) as? ReplyRVAdapter.ReplyViewHolder
                    holder?.updateSubReplyLikeState(effect.subReplyId, false)
                }
            }
            is ReplyEffect.SaveLikeSubReply -> {
//                viewModel.getReply(args.reportId)
                // 어댑터에서 좋아요 업데이트
                adapter.replyList.find { it.replyId == effect.upReplyId }?.let {
                    val position = adapter.replyList.indexOf(it)
                    val holder =
                        binding.recyclerView
                            .findViewHolderForAdapterPosition(position) as? ReplyRVAdapter.ReplyViewHolder
                    holder?.updateSubReplyLikeState(effect.subReplyId, true)
                }
            }

            is ReplyEffect.SaveSubReply -> {
                // 다시 댓글 아이콘 가져오기
                isReplyingToComment = false
                binding.editReply.hint = "댓글 달기"
                adapter.setReplyList(viewModel.replyListStateFlow.value)
            }
            is ReplyEffect.SaveComplaint -> {
                Toast.makeText(context, "댓글을 신고했어요!", Toast.LENGTH_SHORT).show()
            }
            is ReplyEffect.SaveBlock -> {
                Toast.makeText(context, "댓글을 차단했어요!", Toast.LENGTH_SHORT).show()
            }
            is ReplyEffect.SaveLike -> {
                adapter.updateLikeState(viewModel.replyListStateFlow.value)
            }
            is ReplyEffect.CancelLike -> {
                adapter.updateLikeState(viewModel.replyListStateFlow.value)
            }

            is ReplyEffect.ScrollToTop -> {
                binding.recyclerView.scrollToPosition(viewModel.replyListStateFlow.value.size - 1)
            }
            is ReplyEffect.DeleteReply -> {
                adapter.removeReplyItem(effect.position)
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
