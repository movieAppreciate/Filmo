package com.teamfilmo.filmo.ui.reply.adapter

import androidx.recyclerview.widget.RecyclerView
import com.teamfilmo.filmo.databinding.SubReplyItemBinding
import timber.log.Timber

class SubReplyViewHolder(
    private val binding: SubReplyItemBinding,
    private val itemClick: SubReplyItemClick?,
) : RecyclerView.ViewHolder(binding.root) {
    val subReplyContent = binding.txtReply
    val userName = binding.userId
    val writeTime = binding.txtTime
    val likeCount = binding.txtLikeCount

    init {
        binding.btnMeatBall.setOnClickListener {
            Timber.d("답글 미트볼 클릭됨")
            itemClick?.onMeatBallClick(adapterPosition)
        }
    }
}
