package com.teamfilmo.filmo.ui.reply.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.teamfilmo.filmo.data.remote.model.reply.get.GetReplyResponseItem
import com.teamfilmo.filmo.databinding.ReplyItemBinding
import timber.log.Timber

class ReplyViewHolder(
    private val binding: ReplyItemBinding,
    private val itemClick: ReplyItemClick?,
) : RecyclerView.ViewHolder(binding.root) {
    private val subReplyAdapter = SubReplyRVAdapter()

    init {
        with(binding) {
            subReplyRecycerView.adapter = subReplyAdapter
            btnReply.setOnClickListener {
                itemClick?.onReplyClick(adapterPosition)
            }
            btnMore.setOnClickListener {
                itemClick?.onMeatBallClick(adapterPosition)
            }
        }
        subReplyAdapter.subReplyItemClick =
            object : SubReplyItemClick {
                override fun onMeatBallClick(position: Int) {
                    itemClick?.onShowBottomSheet(subReplyAdapter.subReplyList[position].replyId, position)
                }
            }
    }

    fun bind(reply: GetReplyResponseItem) {
        binding.txtReply.text = reply.content
        binding.txtReplyCount.text = reply.subReply?.size?.toString() ?: "0"

        binding.userId.text = reply.nickname
        binding.txtTime.text = reply.createDate

        binding.btnCloseSubReply.setOnClickListener {
            reply.subReply?.take(1)?.let { it1 -> subReplyAdapter.setSubReply(it1) }
            binding.btnCloseSubReply.visibility = View.GONE
            binding.btnReplyMore.visibility = View.VISIBLE
        }

        // fixme : 현재 댓글에 답글이 없는 경우 답글이 없다고 인식된다.

        reply.subReply?.let { subReplyList ->
            Timber.d("답글 리스트 : $subReplyList")
            if (subReplyList.isEmpty()) {
                Timber.d("답글이 없습니다")
                return
            }

            binding.subReplyRecycerView.visibility = View.VISIBLE
            subReplyAdapter.setSubReply(subReplyList.take(1))

            if (subReplyList.size > 1) {
                binding.btnReplyMore.apply {
                    visibility = View.VISIBLE
                    setOnClickListener {
                        subReplyAdapter.setSubReply(subReplyList)
                        visibility = View.GONE
                        binding.btnCloseSubReply.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}
