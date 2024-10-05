package com.teamfilmo.filmo.ui.reply.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamfilmo.filmo.data.remote.model.reply.get.GetReplyResponseItem
import com.teamfilmo.filmo.data.remote.model.reply.get.SubReplyResponse
import com.teamfilmo.filmo.data.remote.model.reply.save.SaveReplyResponse
import com.teamfilmo.filmo.databinding.ReplyItemBinding
import timber.log.Timber

interface ReplyItemClick {
    fun onReplyClick(
        position: Int,
    )

    fun onMeatBallClick(
        position: Int,
    )

    fun onSubReplyDelete(
        replyId: String,
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

    private val subReplyAdapter = SubReplyRVAdapter()

    /*
    답글 추가
     */
    fun addSubReply(
        upReplyId: String,
        newSubReply: SaveReplyResponse,
    ) {
        val newSubReplyItem =
            newSubReply.upReplyId?.let {
                SubReplyResponse(
                    content = newSubReply.content,
                    createDate = "방금",
                    lastModifiedDate = "",
                    nickname = newSubReply.userId,
                    replyId = newSubReply.replyId,
                    reportId = newSubReply.reportId,
                    upReplyId = it,
                    userId = newSubReply.userId,
                )
            }
        val replyPosition = replyList.indexOfFirst { it.replyId == upReplyId }
        Timber.d("답글 추가로 인해 변경된 댓글 아이템 위치, : $replyPosition ")
        if (replyPosition != -1) {
            if (newSubReplyItem != null) {
                // todo : subReply에 전달
                val updateReply =
                    replyList[replyPosition].copy(
                        subReply = (replyList[replyPosition].subReply ?: listOf()) + newSubReplyItem,
                    )
                replyList[replyPosition] = updateReply
                notifyItemChanged(replyPosition)
            }
        }
    }

    /*
    댓글 삭제
     */
    fun removeReplyItem(position: Int) {
        this.replyList.removeAt(position)
        notifyItemRemoved(position)
    }

    /*
    답글 삭제 시 position: 댓글의 id
     */
    fun removeSubReplyItem(
        position: Int,
        subReplyPosition: Int,
    ) {
        val replyItem = replyList[position]
        replyItem.subReply?.let {
            val subReplyList = it.toMutableList()
            Timber.d("삭제된 답글 : ${subReplyList.get(subReplyPosition).content}")
            subReplyList.removeAt(subReplyPosition)
            replyList[position] = replyItem.copy(subReply = subReplyList)
            notifyItemChanged(position)
            setReplyList(replyList)
            subReplyAdapter.setSubReply(subReplyList)
        }
    }

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
                createDate = "방금",
                lastModifiedDate = "",
                nickname = replyItem.userId,
                replyId = replyItem.replyId,
                reportId = replyItem.reportId,
                userId = replyItem.userId,
            )
        this.replyList.add(replyList.lastIndex + 1, newReplyItem)
        notifyItemInserted(replyList.lastIndex + 1)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ReplyViewHolder {
        val binding = ReplyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReplyViewHolder(binding, itemClick, subReplyItemClick)
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
