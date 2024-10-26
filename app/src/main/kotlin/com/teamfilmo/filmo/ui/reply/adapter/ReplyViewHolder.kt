package com.teamfilmo.filmo.ui.reply.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.teamfilmo.filmo.R
import com.teamfilmo.filmo.data.remote.model.reply.get.GetReplyResponseItemWithRole
import com.teamfilmo.filmo.data.remote.model.reply.get.SubReplyResponseWithRole
import com.teamfilmo.filmo.databinding.ReplyItemBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs
import timber.log.Timber

class ReplyViewHolder(
    private val currenUserId: String,
    private val binding: ReplyItemBinding,
    private val itemClick: ReplyItemClick?,
) : RecyclerView.ViewHolder(binding.root) {
    private val subReplyAdapter = SubReplyRVAdapter()

    init {
        with(binding) {
            subReplyRecycerView.adapter = subReplyAdapter
            btnLike.setOnClickListener {
                itemClick?.onLikeClick(adapterPosition)
            }
            btnReply.setOnClickListener {
                itemClick?.onReplyClick(adapterPosition)
            }
            btnMore.setOnClickListener {
                itemClick?.onMeatBallClick(adapterPosition)
            }
        }
        subReplyAdapter.subReplyItemClick =
            object : SubReplyItemClick {
                override fun onMeatBallClick(position: Int) {
                    itemClick?.onShowBottomSheet(subReplyAdapter.subReplyList[position].replyId, position)
                }
            }
    }

    private fun formatTimeDifference(dateString: String): String {
        val possiblePatterns =
            listOf(
                "MMM d, yyyy, h:mm:ss a",
                "MMM dd, yyyy, h:mm:ss a",
                "MMM d, yyyy, hh:mm:ss a",
                "MMM dd, yyyy, hh:mm:ss a",
            )
        var inputDate: Date? = null

        for (pattern in possiblePatterns) {
            try {
                val formatter = SimpleDateFormat(pattern, Locale.ENGLISH)
                formatter.isLenient = false
                inputDate = formatter.parse(dateString)
                break
            } catch (e: ParseException) {
                continue
            }
        }

        if (inputDate == null) {
            return "Invalid Date"
        }

        val currentDate = Date()
        val diffInMills = currentDate.time - inputDate.time
        val diffInSeconds = abs(diffInMills / 1000)
        val diffinMinutes = diffInSeconds / 60
        val diffiInHours = diffinMinutes / 60

        return when {
            diffInSeconds < 60 -> "${diffInSeconds}초 전"
            diffinMinutes < 60 -> "${diffinMinutes}분 전"
            diffiInHours < 24 -> "${diffiInHours}시간 전"
            else -> {
                val outputFormatter = SimpleDateFormat("yy.MM.dd", Locale.KOREA)
                outputFormatter.format(inputDate)
            }
        }
    }

    fun bind(reply: GetReplyResponseItemWithRole) {
        binding.txtReply.text = reply.content
        binding.txtReplyCount.text = reply.subReply?.size?.toString() ?: "0"

        binding.userId.text = reply.nickname
        binding.txtTime.text = formatTimeDifference(reply.createDate)

        // 본인 댓글이 아니라면 삭제 불가능하도록 하기
        if (reply.isMyReply) {
            binding.btnMore.visibility = View.VISIBLE
        } else {
            binding.btnMore.visibility = View.GONE
        }

        if (reply.isLiked) {
            binding.btnLike.setImageResource(R.drawable.ic_like_selected)
        } else {
            binding.btnLike.setImageResource(R.drawable.ic_like_unselected)
        }
        // 좋아요 수 반영
        binding.txtLikeCount.text = reply.likeCount.toString()

        Timber.d("답글 :${reply.subReply}")

        val subReplyListWithRole =
            reply.subReply?.map {
                SubReplyResponseWithRole(
                    content = it.content,
                    createDate = it.createDate,
                    lastModifiedDate = it.lastModifiedDate,
                    nickname = it.nickname,
                    replyId = it.replyId,
                    reportId = it.reportId,
                    upReplyId = it.upReplyId,
                    userId = it.userId,
                    isMySubReply = it.userId == currenUserId,
                )
            }
        reply.subReply?.let {
            if (subReplyListWithRole?.size == 0) {
                subReplyAdapter.setSubReply(subReplyListWithRole)
                binding.btnReplyMore.visibility = View.GONE
                binding.btnCloseSubReply.visibility = View.GONE
                return
            }

            binding.subReplyRecycerView.visibility = View.VISIBLE
            subReplyListWithRole?.let { it1 -> subReplyAdapter.setSubReply(it1.take(1)) }
            binding.btnReplyMore.visibility = View.GONE

            if (subReplyListWithRole?.size!! > 1) {
                with(binding) {
                    btnReplyMore.apply {
                        visibility = View.VISIBLE
                        setOnClickListener {
                            subReplyAdapter.setSubReply(subReplyListWithRole)
                            visibility = View.GONE
                            binding.btnCloseSubReply.visibility = View.VISIBLE
                        }
                    }

                    btnCloseSubReply.setOnClickListener {
                        subReplyListWithRole?.take(1).let {
                            if (it != null) {
                                subReplyAdapter.setSubReply(it)
                            }
                        }
                        binding.btnCloseSubReply.visibility = View.GONE
                        binding.btnReplyMore.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}
