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
import timber.log.Timber

@AndroidEntryPoint
class ReplyFragment : BaseFragment<FragmentReplyBinding, ReplyViewModel, ReplyEffect, ReplyEvent>(
    FragmentReplyBinding::inflate,
) {
    private var isReplyingToComment = false
    private var upReplyId = ""

    override val viewModel: ReplyViewModel by viewModels()
    val adapter by lazy {
        ReplyRVAdapter()
    }

    fun showDeleteDialog(
        reportId: String,
        replyId: String,
        position: Int,
    ) {
        val dialog =
            context?.let {
                CustomDialog(resources.getString(R.string.txt_delete_reply), it)
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
        dialog?.show()
    }

    fun showDeleteSubReplyDialog(
        reportId: String,
        replyId: String,
        position: Int,
    ) {
        val dialog =
            context?.let {
                CustomDialog(resources.getString(R.string.txt_delete_sub_reply), it)
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
        dialog?.show()
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
        val reportId = arguments?.getString("REPORT_ID") ?: ""
        binding.recyclerView.adapter = adapter

        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        adapter.itemClick =
            object : ReplyItemClick {
                override fun onLikeClick(position: Int) {
                    Timber.d("좋아요 클릭")
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

                override fun onMeatBallClick(position: Int) {
                    val bottomSheet =
                        ModalBottomSheet.newInstance(
                            listOf("삭제하기", "취소"),
                        )

                    bottomSheet.show(parentFragmentManager, ModalBottomSheet.TAG)
                    bottomSheet.setListener(
                        object : OnButtonSelectedListener {
                            override fun onButtonSelected(text: String) {
                                val reply = adapter.replyList.get(position).replyId
                                when (text) {
                                    "삭제하기" -> {
                                        showDeleteDialog(reportId, reply, position)
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
                                        showDeleteSubReplyDialog(reportId, replyId, position)
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
                viewModel.handleEvent(ReplyEvent.SaveSubReply(upReplyId, reportId, binding.editReply.text.toString()))
                Toast.makeText(context, "답글이 등록되었어요!", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.handleEvent(ReplyEvent.SaveReply(null, reportId, binding.editReply.text.toString()))
                Toast.makeText(context, "댓글이 등록되었어요!", Toast.LENGTH_SHORT).show()
            }
            binding.editReply.clearAnimation()
            binding.editReply.text.clear()
        }
        /*
        전체 댓글 가져오기
         */
        lifecycleScope.launch {
            viewModel.getReply(reportId)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.replyListStateFlow.collect {
                    binding.txtReplyCount.text = it.size.toString()
                    adapter.setReplyList(it)
                }
            }
        }
    }

    override fun handleEffect(effect: ReplyEffect) {
        when (effect) {
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
        fun newInstance(
            reportId: String,
        ): ReplyFragment {
            val fragment =
                ReplyFragment().apply {
                    arguments =
                        Bundle().apply {
                            putString("REPORT_ID", reportId)
                        }
                }
            return fragment
        }
    }
}
