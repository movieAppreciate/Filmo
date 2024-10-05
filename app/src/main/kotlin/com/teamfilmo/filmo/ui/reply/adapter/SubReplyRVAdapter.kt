package com.teamfilmo.filmo.ui.reply.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamfilmo.filmo.data.remote.model.reply.get.SubReplyResponse
import com.teamfilmo.filmo.databinding.SubReplyItemBinding
import timber.log.Timber

interface SubReplyItemClick {
    fun onMeatBallClick(
        position: Int,
    )
}

class SubReplyRVAdapter : RecyclerView.Adapter<SubReplyViewHolder>() {
    val subReplyList: ArrayList<SubReplyResponse> = arrayListOf()

    /*
    답글 삭제
     */
    fun removeSubReply(position: Int) {
        this.subReplyList.removeAt(position)
        notifyItemRemoved(position)
    }

    /*
    답글 작성
     */
    fun addSubReply(subReplyItem: SubReplyResponse) {
        Timber.d("subReplyAdapter addSubReply 호출됨")
        this.subReplyList.add(subReplyItem)
        Timber.d("subReplyList : ${this.subReplyList}")
        notifyItemInserted(subReplyList.lastIndex)
    }

    fun setSubReply(subReplyList: List<SubReplyResponse>) {
        Timber.d("subReplyAdapter setSubReply 호출됨")
        this.subReplyList.clear()
        this.subReplyList.addAll(subReplyList)
        Timber.d("전체 답글 리스트 : $subReplyList")
        notifyDataSetChanged()
    }

    var subReplyItemClick: SubReplyItemClick? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SubReplyViewHolder {
        val binding = SubReplyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubReplyViewHolder(binding, subReplyItemClick)
    }

    override fun onBindViewHolder(
        holder: SubReplyViewHolder,
        position: Int,
    ) {
        holder.subReplyContent.text = subReplyList[position].content
        holder.userName.text = subReplyList[position].nickname
        holder.writeTime.text = subReplyList[position].createDate
    }

    override fun getItemCount(): Int = subReplyList.size
}
