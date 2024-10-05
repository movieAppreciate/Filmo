package com.teamfilmo.filmo.ui.reply.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamfilmo.filmo.data.remote.model.reply.get.GetReplyResponseItem
import com.teamfilmo.filmo.databinding.ReplyItemBinding

interface ReplyItemClick {
    fun onReplyClick(
        position: Int,
    )

    fun onMeatBallClick(
        position: Int,
    )

    fun onShowBottomSheet(
        replyId: String,
        position: Int,
    )
}

class ReplyRVAdapter : RecyclerView.Adapter<ReplyViewHolder>() {
    val replyList: ArrayList<GetReplyResponseItem> = arrayListOf()
    var itemClick: ReplyItemClick? = null
    private var subReplyItemClick: SubReplyItemClick? = null

    /*
    댓글 삭제
     */
    fun removeReplyItem(position: Int) {
        this.replyList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun setReplyList(replyList: List<GetReplyResponseItem>) {
        val list = replyList
        val currentSize = this.replyList.size
        this.replyList.clear()
        this.replyList.addAll(list)
        notifyItemRangeRemoved(0, currentSize)
        notifyItemRangeInserted(0, replyList.size)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ReplyViewHolder {
        val binding = ReplyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReplyViewHolder(binding, itemClick)
    }

    override fun onBindViewHolder(
        holder: ReplyViewHolder,
        position: Int,
    ) {
        val reply = replyList[position]
        holder.bind(reply)
    }

    override fun getItemCount(): Int = replyList.size
}
