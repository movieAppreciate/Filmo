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

        // fixme : 현재 댓글에 답글이 없는 경우 답글이 없다고 인식된다.

        Timber.d("답글 리스트 : ${reply.subReply}")
        reply.subReply?.let { subReplyList ->
            Timber.d("답글 리스트 : $subReplyList")
            if (subReplyList.size == 0) {
                Timber.d("답글이 없습니다")
                subReplyAdapter.setSubReply(subReplyList)
                binding.btnReplyMore.visibility = View.GONE
                binding.btnCloseSubReply.visibility = View.GONE
                return
            }

            binding.subReplyRecycerView.visibility = View.VISIBLE
            // fixme : 댓글이 새로 등록되면 답글 리스트가 업데이트 되어야함.
            subReplyAdapter.setSubReply(subReplyList.take(1))
            binding.btnReplyMore.visibility = View.GONE

            if (subReplyList.size > 1) {
                with(binding) {
                    btnReplyMore.apply {
                        visibility = View.VISIBLE
                        setOnClickListener {
                            subReplyAdapter.setSubReply(subReplyList)
                            visibility = View.GONE
                            binding.btnCloseSubReply.visibility = View.VISIBLE
                        }
                    }
                }

                with(binding) {
                    btnCloseSubReply.setOnClickListener {
                        reply.subReply.take(1).let {
                            subReplyAdapter.setSubReply(it)
                        }
                        binding.btnCloseSubReply.visibility = View.GONE
                        binding.btnReplyMore.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}
