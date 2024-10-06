package com.teamfilmo.filmo.ui.reply.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.teamfilmo.filmo.data.remote.model.reply.get.GetReplyResponseItem
import com.teamfilmo.filmo.databinding.ReplyItemBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs

class ReplyViewHolder(
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

    fun bind(reply: GetReplyResponseItem) {
        binding.txtReply.text = reply.content
        binding.txtReplyCount.text = reply.subReply?.size?.toString() ?: "0"

        binding.userId.text = reply.nickname
        binding.txtTime.text = formatTimeDifference(reply.createDate)

        // 구현 : 시간 로직 추가하기

        reply.subReply?.let { subReplyList ->
            if (subReplyList.size == 0) {
                subReplyAdapter.setSubReply(subReplyList)
                binding.btnReplyMore.visibility = View.GONE
                binding.btnCloseSubReply.visibility = View.GONE
                return
            }

            binding.subReplyRecycerView.visibility = View.VISIBLE
            subReplyAdapter.setSubReply(subReplyList.take(1))
            binding.btnReplyMore.visibility = View.GONE

            if (subReplyList.size > 1) {
                with(binding) {
                    btnReplyMore.apply {
                        visibility = View.VISIBLE
                        setOnClickListener {
                            subReplyAdapter.setSubReply(subReplyList)
                            visibility = View.GONE
                            binding.btnCloseSubReply.visibility = View.VISIBLE
                        }
                    }

                    btnCloseSubReply.setOnClickListener {
                        reply.subReply.take(1).let {
                            subReplyAdapter.setSubReply(it)
                        }
                        binding.btnCloseSubReply.visibility = View.GONE
                        binding.btnReplyMore.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}
