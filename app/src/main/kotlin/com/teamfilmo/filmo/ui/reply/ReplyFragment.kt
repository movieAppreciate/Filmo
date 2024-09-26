package com.teamfilmo.filmo.ui.reply

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.teamfilmo.filmo.R
import com.teamfilmo.filmo.base.fragment.BaseFragment
import com.teamfilmo.filmo.databinding.FragmentReplyBinding
import com.teamfilmo.filmo.ui.reply.adapter.ReplyRecyclerViewAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class ReplyFragment : BaseFragment<FragmentReplyBinding, ReplyViewModel, ReplyEffect, ReplyEvent>(
    FragmentReplyBinding::inflate,
) {
    private var isReplyingToComment = false

    override val viewModel: ReplyViewModel by viewModels()
    val adapter by lazy {
        ReplyRecyclerViewAdapter()
    }

    override fun onBindLayout() {
        val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        val reportId = arguments?.getString("REPORT_ID") ?: ""
        binding.recyclerView.adapter = adapter
        binding.recyclerView.scrollToPosition(0)

        binding.root.setOnClickListener {
            binding.editReply.hint = "댓글달기"
        }
        adapter.itemClick =
            object : ReplyRecyclerViewAdapter.ReplyItemClick {
                override fun onReplyClick(position: Int) {
                    val reply = adapter.replyList[position]
                    // todo :1.  키보드 올리기  2. 답글 hint 수정
                    isReplyingToComment = true
                    binding.editReply.requestFocus()
                    inputMethodManager.showSoftInput(binding.editReply, InputMethodManager.SHOW_FORCED)
                    binding.editReply.hint = "${reply.nickname}에게 답글 작성하기"
                }
            }
        binding.editReply.setOnClickListener {
            binding.btnRegistReply.setImageResource(R.drawable.btn_save_reply)
        }
        binding.btnRegistReply.setOnClickListener {
            viewModel.handleEvent(ReplyEvent.SaveReply(reportId, binding.editReply.text.toString()))
            // todo : edittext 창 초기화
            binding.editReply.clearAnimation()
            binding.editReply.text.clear()
            Toast.makeText(context, "댓글이 등록되었어요!", Toast.LENGTH_SHORT).show()
        }
        lifecycleScope.launch {
            viewModel.getReply(reportId)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.replyListStateFlow.collect {
                    binding.txtReplyCount.text = it.size.toString()
                    Timber.d("reply data : $it")
                    adapter.setReplyList(it)
                }
            }
        }
    }

    override fun handleEffect(effect: ReplyEffect) {
        when (effect) {
            is ReplyEffect.SaveReply -> {
                // todo : 리사이클러뷰에 아이템 업데이트
                lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.replyItemStateFlow.collect {
                            Timber.d("추가된 댓글 : $it")
                            adapter.addReply(it)
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
