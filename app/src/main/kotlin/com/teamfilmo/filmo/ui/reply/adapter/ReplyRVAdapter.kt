package com.teamfilmo.filmo.ui.reply.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamfilmo.filmo.data.remote.model.reply.get.GetReplyResponseItem
import com.teamfilmo.filmo.data.remote.model.reply.save.SaveReplyResponse
import com.teamfilmo.filmo.databinding.ReplyItemBinding

class ReplyRVAdapter : RecyclerView.Adapter<ReplyRVAdapter.ReplyViewHolder>() {
    val replyList: ArrayList<GetReplyResponseItem> = arrayListOf()

    interface ReplyItemClick {
        fun onReplyClick(
            position: Int,
        )
    }

    var itemClick: ReplyItemClick? = null

    fun setReplyList(replyList: List<GetReplyResponseItem>) {
        val list = replyList
        val currentSize = this.replyList.size
        this.replyList.clear()
        this.replyList.addAll(list)
        notifyItemRangeRemoved(0, currentSize)
        notifyItemRangeInserted(0, replyList.size)
    }

    fun addReply(replyItem: SaveReplyResponse) {
        val newReplyItem =
            GetReplyResponseItem(
                content = replyItem.content,
                createDate = "",
                lastModifiedDate = "",
                nickname = "",
                replyId = replyItem.replyId,
                reportId = replyItem.reportId,
                userId = replyItem.userId,
            )
        this.replyList.add(replyList.size, newReplyItem)
        notifyItemInserted(replyList.size)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ReplyViewHolder {
        val binding = ReplyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReplyViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ReplyViewHolder,
        position: Int,
    ) {
        val reply = replyList[position]
        holder.bind(reply)
    }

    override fun getItemCount(): Int = replyList.size

    inner class ReplyViewHolder(private val binding: ReplyItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val subReplyAdapter = SubReplyRVAdapter()

        init {
            binding.subReplyRecycerView.apply {
                adapter = subReplyAdapter
            }

            binding.btnReply.setOnClickListener {
                itemClick?.onReplyClick(adapterPosition)
            }
        }

        fun bind(reply: GetReplyResponseItem) {
            binding.txtReply.text = reply.content
            binding.txtReplyCount.text = reply.subReply?.size?.toString() ?: "0"

            binding.userId.text = reply.nickname
            binding.txtTime.text = reply.createDate

            reply.subReply?.let {
                subReplyAdapter.setSubReply(it)
                binding.subReplyRecycerView.visibility = if (it.isNotEmpty()) View.VISIBLE else View.GONE
            }
        }
    }
}
