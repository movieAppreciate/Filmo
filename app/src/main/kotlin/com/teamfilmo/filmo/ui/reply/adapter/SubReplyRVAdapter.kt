package com.teamfilmo.filmo.ui.reply.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamfilmo.filmo.data.remote.model.reply.get.SubReplyResponse
import com.teamfilmo.filmo.databinding.SubReplyItemBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs

interface SubReplyItemClick {
    fun onMeatBallClick(
        position: Int,
    )
}

class SubReplyRVAdapter : RecyclerView.Adapter<SubReplyViewHolder>() {
    val subReplyList: ArrayList<SubReplyResponse> = arrayListOf()

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
        holder.writeTime.text = formatTimeDifference(subReplyList[position].createDate)
    }

    override fun getItemCount(): Int = subReplyList.size
}
