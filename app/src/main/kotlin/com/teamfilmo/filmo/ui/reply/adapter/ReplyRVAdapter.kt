package com.teamfilmo.filmo.ui.reply.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.teamfilmo.filmo.data.remote.model.reply.get.GetReplyResponseItemWithRole
import com.teamfilmo.filmo.databinding.ItemReplyBinding
import com.teamfilmo.filmo.ui.reply.diffutil.ReplyDiffCallBack

interface ReplyItemClick {
    fun onLikeClick(
        position: Int,
    )

    fun onReplyClick(
        position: Int,
    )

    fun onMeatBallClick(
        isMyReply: Boolean,
        position: Int,
    )

    fun onShowBottomSheet(
        replyId: String,
        position: Int,
    )
}

class ReplyRVAdapter : RecyclerView.Adapter<ReplyViewHolder>() {
    val replyList: ArrayList<GetReplyResponseItemWithRole> = arrayListOf()
    var itemClick: ReplyItemClick? = null
    private var currentUserId: String = ""

    fun setUserId(userId: String) {
        this.currentUserId = userId
    }

    /*
    좋아요 등록
     */
    fun updateLikeState(
        newList: List<GetReplyResponseItemWithRole>,
    ) {
        val diffCallBack = ReplyDiffCallBack(this.replyList, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)
        replyList.clear()
        replyList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    /*
    댓글 삭제
     */
    fun removeReplyItem(position: Int) {
        this.replyList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun setReplyList(replyList: List<GetReplyResponseItemWithRole>) {
        val diffCallBack = ReplyDiffCallBack(this.replyList, replyList)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)
        this.replyList.clear()
        this.replyList.addAll(replyList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ReplyViewHolder {
        val binding = ItemReplyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReplyViewHolder(currentUserId, binding, itemClick)
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
