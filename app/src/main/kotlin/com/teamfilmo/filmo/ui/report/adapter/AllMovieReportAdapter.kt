package com.teamfilmo.filmo.ui.report.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.teamfilmo.filmo.R
import com.teamfilmo.filmo.databinding.ItemReportBinding
import com.teamfilmo.filmo.domain.model.report.all.ReportItem
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs

sealed class ReportPayload {
    data class ModifyReportPayLoad(
        val title: String,
        val content: String,
    ) : ReportPayload()

    data class LikePayload(
        val isLiked: Boolean,
        val likeCount: Int,
    ) : ReportPayload()

    data class ReplyCountPayLoad(
        val replyCount: Int,
    ) : ReportPayload()
}

class AllMovieReportAdapter : PagingDataAdapter<ReportItem, AllMovieReportAdapter.AllMovieReportViewHolder>(DIFF_CALLBACK) {
// 1. reportList 변수 제거
    // getItem(position) 을 통해 아이템 접근
    // snapshot().items 를 통해 현재 표시된 아이템의 목록에 접근
    //    var reportList: ArrayList<ReportItem> = arrayListOf()

    companion object {
        private val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<ReportItem>() {
                override fun areItemsTheSame(
                    oldItem: ReportItem,
                    newItem: ReportItem,
                ): Boolean = oldItem.reportId == newItem.reportId

                override fun areContentsTheSame(
                    oldItem: ReportItem,
                    newItem: ReportItem,
                ): Boolean =
                    oldItem.reportId == newItem.reportId &&
                        oldItem.likeCount == newItem.likeCount &&
                        oldItem.isLiked == newItem.isLiked &&
                        // 상세 내용 비교
                        oldItem.replyCount == newItem.replyCount
            }
    }

    interface ItemClick {
        fun onClick(report: ReportItem)

        fun onLikeClick(report: ReportItem)
    }

    fun updateLikeState(
        reportId: String,
        isLiked: Boolean,
        likeCount: Int,
    ) {
        val position = snapshot().items.indexOfFirst { it.reportId == reportId }
        if (position != -1) {
            getItem(position)?.let {
                it.isLiked = isLiked
                it.likeCount = likeCount
                notifyItemChanged(position, ReportPayload.LikePayload(isLiked, likeCount))
            }
        }
    }

    fun updateReplyCount(
        reportId: String,
        replyCount: Int,
    ) {
        val position = snapshot().items.indexOfFirst { it.reportId == reportId }
        if (position != -1) {
            getItem(position)?.let {
                it.replyCount = replyCount
                notifyItemChanged(position, ReportPayload.ReplyCountPayLoad(replyCount))
            }
        }
    }

    fun updateModifyReport(
        reportId: String,
        title: String,
        content: String,
    ) {
        val position = snapshot().items.indexOfFirst { it.reportId == reportId }
        if (position != -1) {
            getItem(position)?.let {
                it.title = title
                it.content = content
                notifyItemChanged(position, ReportPayload.ModifyReportPayLoad(title, content))
            }
        }
    }

    var itemClick: ItemClick? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): AllMovieReportViewHolder {
        val binding = ItemReportBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AllMovieReportViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: AllMovieReportViewHolder,
        position: Int,
    ) {
        getItem(position)?.let { item ->
            holder.bindItems(item)
            holder.bindLikeImage(item.isLiked)
            holder.bindLikeCount(item.likeCount)
            holder.bindMovieImage(item.imageUrl.toString())
            holder.itemView.apply {
            }
        }
    }

    override fun onBindViewHolder(
        holder: AllMovieReportViewHolder,
        position: Int,
        payloads: MutableList<Any>,
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            payloads.forEach {
                when (val payload = it as ReportPayload) {
                    is ReportPayload.LikePayload -> {
                        getItem(position)?.isLiked = payload.isLiked
                        getItem(position)?.likeCount = payload.likeCount
                        holder.bindLikeButton(if (payload.isLiked) R.drawable.ic_like_selected else R.drawable.ic_like_unselected)
                        holder.bindLikeCount(payload.likeCount)
                    }

                    is ReportPayload.ReplyCountPayLoad -> {
                        getItem(position)?.replyCount = payload.replyCount
                        holder.bindReplyCount(payload.replyCount)
                    }
                    is ReportPayload.ModifyReportPayLoad -> {
                        holder.bindModifyReport(payload.title, payload.content)
                    }
                    else -> {}
                }
            }
        }
    }

    inner class AllMovieReportViewHolder(
        private val binding: ItemReportBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.reportCardView.setOnClickListener {
                getItem(position)?.let { report ->
                    itemClick?.onClick(report)
                }
            }
            binding.likeButton.setOnClickListener {
                getItem(position)?.let { report ->
                    itemClick?.onLikeClick(report)
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

        fun bindItems(item: ReportItem) {
            val title = binding.tvTitle
            val content = binding.tvContent
            val replyCount = binding.tvReplyCount
            val likeCount = binding.tvLikeCount
            val nickName = binding.tvNickName

            title.text = item.title
            content.text = item.content
            replyCount.text = item.replyCount.toString()
            nickName.text = item.nickname ?: "익명의 리뷰어"
            likeCount.text = item.likeCount.toString()
            binding.tvCreateDate.text = formatTimeDifference(item.createDate)
            binding.tvMovie.text = item.movieName

            bindMovieImage(item.imageUrl.toString())
        }

        fun bindModifyReport(
            title: String,
            content: String,
        ) {
            binding.tvTitle.text = title
            binding.tvContent.text = content
        }

        fun bindReplyCount(count: Int) {
            binding.tvReplyCount.text = count.toString()
        }

        fun bindMovieImage(imageUrl: String) {
            Glide
                .with(binding.root.context)
                .load(imageUrl)
                .into(binding.movieImage)
        }

        fun bindLikeButton(imageSrc: Int) {
            binding.likeButton.setImageResource(imageSrc)
        }

        fun bindLikeCount(count: Int) {
            val likeCount = binding.tvLikeCount
            likeCount.text = count.toString()
        }

        fun bindLikeImage(isLiked: Boolean) {
            if (isLiked) {
                binding.likeButton.setImageResource(R.drawable.ic_like_selected)
            } else {
                binding.likeButton.setImageResource(R.drawable.ic_like)
            }
        }
    }
}
