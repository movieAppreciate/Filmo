package com.teamfilmo.filmo.ui.report.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.teamfilmo.filmo.R
import com.teamfilmo.filmo.data.remote.model.report.all.ReportItem
import com.teamfilmo.filmo.databinding.ReportItemBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs

sealed class ReportPayload {
    data class LikePayload(
        val isLiked: Boolean,
    ) : ReportPayload()

    data class LikeCountPayload(
        val likeCount: Int,
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
                ): Boolean = oldItem == newItem
            }
    }

    interface ItemClick {
        fun onClick(report: ReportItem)

        fun onLikeClick(report: ReportItem)
    }

    fun updateLikeState(
        reportId: String,
        isLiked: Boolean,
    ) {
        val position = snapshot().items.indexOfFirst { it.reportId == reportId }
        if (position != -1) {
            getItem(position)?.let {
                it.isLiked = isLiked
                notifyItemChanged(position, ReportPayload.LikePayload(isLiked))
            }
        }
    }

    fun updateLikeCount(
        reportId: String,
        likeCount: Int,
    ) {
        val position = snapshot().items.indexOfFirst { it.reportId == reportId }
        if (position != -1) {
            getItem(position)?.let {
                it.likeCount = likeCount
                notifyItemChanged(position, ReportPayload.LikeCountPayload(likeCount))
            }
        }
    }

    var itemClick: ItemClick? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): AllMovieReportViewHolder {
        val binding = ReportItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
                        holder.bindLikeButton(if (payload.isLiked) R.drawable.ic_like_selected else R.drawable.ic_like_unselected)
                    }

                    is ReportPayload.LikeCountPayload -> {
                        getItem(position)?.likeCount = payload.likeCount
                        holder.bindLikeCount(payload.likeCount)
                    }
                }
            }
        }
    }

    inner class AllMovieReportViewHolder(
        private val binding: ReportItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.movieImage.setOnClickListener {
                getItem(position)?.let { report ->
                    itemClick?.onClick(report)
                }
            }
            binding.tvTitle.setOnClickListener {
                getItem(position)?.let { report ->
                    itemClick?.onClick(report)
                }
            }
            binding.tvContent.setOnClickListener {
                getItem(position)?.let { report ->
                    itemClick?.onClick(report)
                }
            }
            binding.btnReply.setOnClickListener {
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
            nickName.text = item.nickname
            likeCount.text = item.likeCount.toString()
            binding.tvCreateDate.text = formatTimeDifference(item.createDate)
            binding.tvMovie.text = item.movieName

            bindMovieImage(item.imageUrl.toString())
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
                binding.likeButton.setImageResource(R.drawable.ic_like_unselected)
            }
        }
    }
}
