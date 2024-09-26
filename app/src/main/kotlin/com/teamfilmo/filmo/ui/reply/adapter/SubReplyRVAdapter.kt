package com.teamfilmo.filmo.ui.reply.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamfilmo.filmo.data.remote.model.reply.get.SubReplyResponse
import com.teamfilmo.filmo.databinding.SubReplyItemBinding

class SubReplyRVAdapter : RecyclerView.Adapter<SubReplyRVAdapter.SubReplyViewHolder>() {
    private val subReplyList: ArrayList<SubReplyResponse> = arrayListOf()

    fun setSubReply(subReplyList: List<SubReplyResponse>) {
        this.subReplyList.clear()
        this.subReplyList.addAll(subReplyList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SubReplyViewHolder {
        val binding = SubReplyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubReplyViewHolder(binding)
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

    inner class SubReplyViewHolder(private val binding: SubReplyItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val subReplyContent = binding.txtReply
        val userName = binding.userId
        val writeTime = binding.txtTime
        val likeCount = binding.txtLikeCount
    }
}
