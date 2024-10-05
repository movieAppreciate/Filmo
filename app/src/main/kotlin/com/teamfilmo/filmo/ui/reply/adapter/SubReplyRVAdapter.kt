package com.teamfilmo.filmo.ui.reply.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamfilmo.filmo.data.remote.model.reply.get.SubReplyResponse
import com.teamfilmo.filmo.databinding.SubReplyItemBinding

interface SubReplyItemClick {
    fun onMeatBallClick(
        position: Int,
    )
}

class SubReplyRVAdapter : RecyclerView.Adapter<SubReplyViewHolder>() {
    val subReplyList: ArrayList<SubReplyResponse> = arrayListOf()

    fun setSubReply(subReplyList: List<SubReplyResponse>) {
        this.subReplyList.clear()
        this.subReplyList.addAll(subReplyList)
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
