package com.teamfilmo.filmo.ui.reply.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamfilmo.filmo.R
import com.teamfilmo.filmo.databinding.ItemSubReplyBinding
import com.teamfilmo.filmo.ui.reply.SubReplyInteractionListener
import com.teamfilmo.filmo.ui.reply.SubReplyWithLikeInfo
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs
import timber.log.Timber

class SubReplyRVAdapter : RecyclerView.Adapter<SubReplyRVAdapter.SubReplyViewHolder>() {
    val subReplyList: ArrayList<SubReplyWithLikeInfo> = arrayListOf()
    var subReplyListener: SubReplyInteractionListener? = null

    fun updateItemLikeState(
        subReplyId: String,
        isLiked: Boolean,
    ) {
        val position = subReplyList.indexOfFirst { it.replyId == subReplyId }
        val currentLike = subReplyList[position].likeCount
        if (position == -1) return
        if (isLiked) {
            subReplyList[position] = subReplyList[position].copy(isLiked = isLiked, likeCount = currentLike + 1)
        } else {
            subReplyList[position] = subReplyList[position].copy(isLiked = isLiked, likeCount = currentLike - 1)
        }
        notifyItemChanged(position)
    }

    fun deleteSubReplyItem(
        upReplyId: String,
        subReplyId: String,
    ) {
        val position = subReplyList.indexOfFirst { it.replyId == subReplyId }
        if (position == -1) return
        subReplyList.removeAt(position)
        notifyItemRemoved(position)
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

    fun setSubReply(subReplyList: List<SubReplyWithLikeInfo>) {
        this.subReplyList.clear()
        this.subReplyList.addAll(subReplyList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SubReplyViewHolder {
        val binding = ItemSubReplyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubReplyViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: SubReplyViewHolder,
        position: Int,
    ) {
        Timber.d("전체 답글 리스트 : $subReplyList")
        holder.subReplyContent.text = subReplyList[position].content
        holder.userName.text = subReplyList[position].nickname
        holder.writeTime.text = formatTimeDifference(subReplyList[position].createDate)
        holder.setIsMySubReply(subReplyList[position].isMySubReply)
        holder.likeCount.text = subReplyList[position].likeCount.toString()
        holder.setLikeState(subReplyList[position].isLiked)
    }

    override fun getItemCount(): Int = subReplyList.size

    inner class SubReplyViewHolder(
        private val binding: ItemSubReplyBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        val subReplyContent = binding.txtReply
        val userName = binding.userId
        val writeTime = binding.txtTime
        val likeCount = binding.txtLikeCount
        val createDate = binding.txtTime

        fun setLikeState(isLiked: Boolean) {
            if (isLiked) {
                binding.btnLike.setBackgroundResource(R.drawable.ic_like_selected)
            } else {
                binding.btnLike.setBackgroundResource(R.drawable.ic_like_unselected)
            }
        }

        fun setIsMySubReply(isMySubReply: Boolean) {
            if (isMySubReply) {
                binding.btnMeatBall.visibility = View.VISIBLE
            } else {
                binding.btnMeatBall.visibility = View.GONE
            }
        }

        init {
            with(binding) {
                btnMeatBall.setOnClickListener {
                    Timber.d("더보기 클릭")
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val subReply = subReplyList[position]
                        subReplyListener?.onSubReplyMoreClick(subReply.isMySubReply, subReply.upReplyId, subReply.replyId)
                    }
                }
                btnLike.setOnClickListener {
                    Timber.d("답글 좋아요 클릭 ")
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val subReply = subReplyList[position]
                        subReplyListener?.onSubReplyLikeClick(
                            subReply.upReplyId,
                            subReply.replyId,
                        )
                    }
                }
            }
        }
    }
}
