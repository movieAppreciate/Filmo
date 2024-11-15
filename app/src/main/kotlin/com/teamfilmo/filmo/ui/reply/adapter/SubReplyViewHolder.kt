package com.teamfilmo.filmo.ui.reply.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.teamfilmo.filmo.databinding.ItemSubReplyBinding
import timber.log.Timber

class SubReplyViewHolder(
    private val binding: ItemSubReplyBinding,
    private val itemClick: SubReplyItemClick?,
) : RecyclerView.ViewHolder(binding.root) {
    val subReplyContent = binding.txtReply
    val userName = binding.userId
    val writeTime = binding.txtTime
    val likeCount = binding.txtLikeCount
    val createDate = binding.txtTime

    fun setIsMySubReply(isMySubReply: Boolean) {
        if (isMySubReply) {
            binding.btnMeatBall.visibility = View.VISIBLE
        } else {
            binding.btnMeatBall.visibility = View.GONE
        }
    }

    init {
        binding.btnMeatBall.setOnClickListener {
            Timber.d("답글 미트볼 클릭됨")
            itemClick?.onMeatBallClick(adapterPosition)
        }
    }
}
