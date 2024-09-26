package com.teamfilmo.filmo.ui.reply.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamfilmo.filmo.data.remote.model.reply.get.GetReplyResponseItem
import com.teamfilmo.filmo.data.remote.model.reply.save.SaveReplyResponse
import com.teamfilmo.filmo.databinding.ReplyItemBinding
import timber.log.Timber

class ReplyRecyclerViewAdapter : RecyclerView.Adapter<ReplyRecyclerViewAdapter.ReplyViewHolder>() {
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
        Timber.d("추가된 ReplyList 첫번째 : ${replyList.first()}")
        Timber.d("추가된 ReplyList 마지막 : ${replyList.last()}")
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
        holder.replyTextView.text = replyList[position].content
    }

    override fun getItemCount(): Int = replyList.size

    inner class ReplyViewHolder(private val binding: ReplyItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val replyTextView = binding.txtReply

        init {
            binding.btnReply.setOnClickListener {
                itemClick?.onReplyClick(adapterPosition)
            }
        }
    }
}
