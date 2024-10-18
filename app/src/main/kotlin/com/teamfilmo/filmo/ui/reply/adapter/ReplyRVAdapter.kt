package com.teamfilmo.filmo.ui.reply.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.teamfilmo.filmo.data.remote.model.reply.get.GetReplyResponseItemWithRole
import com.teamfilmo.filmo.databinding.ReplyItemBinding
import com.teamfilmo.filmo.ui.reply.diffutil.ReplyDiffCallBack
import timber.log.Timber

interface ReplyItemClick {
    fun onLikeClick(
        position: Int,
    )

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
    val replyList: ArrayList<GetReplyResponseItemWithRole> = arrayListOf()
    var itemClick: ReplyItemClick? = null
    private var currentUserId: String = ""

    fun setUserId(userId: String) {
        this.currentUserId = userId
        Timber.d("어댑터에 currentUserId : $currentUserId")
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
//        Timber.d("받은 데이터 : $replyList")
//        val list = replyList
//        val currentSize = this.replyList.size
        this.replyList.clear()
        this.replyList.addAll(replyList)
        diffResult.dispatchUpdatesTo(this)
//        notifyItemRangeRemoved(0, currentSize)
//        notifyItemRangeInserted(0, replyList.size)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ReplyViewHolder {
        val binding = ReplyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
