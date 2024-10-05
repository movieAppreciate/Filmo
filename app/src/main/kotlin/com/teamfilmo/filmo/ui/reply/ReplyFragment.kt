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
import com.teamfilmo.filmo.data.remote.model.reply.get.GetReplyResponseItem
import com.teamfilmo.filmo.databinding.FragmentReplyBinding
import com.teamfilmo.filmo.ui.reply.adapter.ReplyItemClick
import com.teamfilmo.filmo.ui.reply.adapter.ReplyRVAdapter
import com.teamfilmo.filmo.ui.reply.adapter.SubReplyRVAdapter
import com.teamfilmo.filmo.ui.widget.ModalBottomSheet
import com.teamfilmo.filmo.ui.widget.OnButtonSelectedListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
    private val subReplyAdapter by lazy {
        SubReplyRVAdapter()
    }

    private fun saveReply(
        item: GetReplyResponseItem,
        reportId: String,
    ) {
        binding.btnRegistReply.setOnClickListener {
            if (isReplyingToComment) {
                Toast.makeText(context, "답글이 등록되었어요!", Toast.LENGTH_SHORT).show()
                viewModel.handleEvent(ReplyEvent.SaveReply(item.replyId, reportId, binding.editReply.text.toString()))
                binding.editReply.clearAnimation()
                binding.editReply.text.clear()
            }
            subReplyAdapter.notifyDataSetChanged()
        }
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
        // todo : 앱 상 뒤로 가기 버튼 말고 고유 뒤로 가기 버튼 동작 시 댓글 작성으로 변환
//        if (isReplyingToComment){
//            isReplyingToComment = false
//            binding.editReply.hint="댓글 달기"
//        }
//

        adapter.itemClick =
            object : ReplyItemClick {
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
                                saveReply(item, reportId)
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
                                        viewModel.handleEvent(ReplyEvent.DeleteReply(reply, reportId))
                                        adapter.removeReplyItem(position)
                                        Toast.makeText(context, "댓글을 삭제했어요!", Toast.LENGTH_SHORT).show()
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
                                        viewModel.handleEvent(ReplyEvent.DeleteSubReply(replyId, reportId))
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
            is ReplyEffect.SaveReply -> {
            }
            is ReplyEffect.SaveSubReply -> {
                lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.subReplyStateFlow.collect {
                            if (it.upReplyId != null) {
                                upReplyId = it.upReplyId
                            }
                        }
                    }
                }
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
