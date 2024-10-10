package com.teamfilmo.filmo.ui.report.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.teamfilmo.filmo.R
import com.teamfilmo.filmo.databinding.MovieItemBinding
import com.teamfilmo.filmo.model.report.ReportItem
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs

sealed class ReportPayload {
    data class BookmarkPayload(var isBookmarked: Boolean) : ReportPayload()

    data class LikePayload(val isLiked: Boolean) : ReportPayload()

    data class LikeCountPayload(val likeCount: Int) : ReportPayload()
}

class AllMovieReportAdapter : RecyclerView.Adapter<AllMovieReportAdapter.AllMovieReportViewHolder>() {
    var reportList: ArrayList<ReportItem> = arrayListOf()

    interface ItemClick {
        fun onClick(
            position: Int,
        )

        fun onLikeClick(position: Int)

        fun onBookmarkClick(position: Int)
    }

    fun updateLikeState(
        reportId: String,
        isLiked: Boolean,
    ) {
        val position = reportList.indexOfFirst { it.reportId == reportId }
        if (position != -1) {
            reportList[position].isLiked = isLiked
            notifyItemChanged(position, ReportPayload.LikePayload(isLiked))
        }
    }

    fun updateLikeCount(
        reportId: String,
        likeCount: Int,
    ) {
        val position = reportList.indexOfFirst { it.reportId == reportId }
        if (position != -1) {
            reportList[position].likeCount = likeCount
            notifyItemChanged(position, ReportPayload.LikeCountPayload(likeCount))
        }
    }

    fun updateBookmarkState(
        reportId: String,
        isBookmarked: Boolean,
    ) {
        val position = reportList.indexOfFirst { it.reportId == reportId }
        if (position != -1) {
            reportList[position].isBookmark = isBookmarked
            notifyItemChanged(position, ReportPayload.BookmarkPayload(isBookmarked))
        }
    }

    var itemClick: ItemClick? = null

    fun setReportInfo(
        newReportList: List<ReportItem>,
    ) {
        val list = newReportList
        val currentSize = reportList.size
        reportList.clear()
        reportList.addAll(list)
        notifyItemRangeRemoved(0, currentSize)
        notifyItemRangeInserted(0, reportList.size)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): AllMovieReportViewHolder {
        val binding = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AllMovieReportViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: AllMovieReportViewHolder,
        position: Int,
    ) {
        holder.bindItems(reportList[position])
        holder.bindLikeImage(reportList[position].isLiked)
        holder.bindLikeCount(reportList[position].likeCount)
        holder.bindBookmarkButton(reportList[position].isBookmark)
        holder.bindMovieImage(reportList[position].imageUrl.toString())
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
                    is ReportPayload.BookmarkPayload -> {
                        this.reportList[position].isBookmark = payload.isBookmarked
                        holder.bindBookmarkButton(payload.isBookmarked)
                    }

                    is ReportPayload.LikePayload -> {
                        this.reportList[position].isLiked = payload.isLiked
                        holder.bindLikeButton(if (payload.isLiked) R.drawable.ic_like_selected else R.drawable.ic_like_unselected)
                    }

                    is ReportPayload.LikeCountPayload -> {
                        this.reportList[position].likeCount = payload.likeCount
                        holder.bindLikeCount(payload.likeCount)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return reportList.size
    }

    inner class AllMovieReportViewHolder(private val binding: MovieItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.movieImage.setOnClickListener {
                itemClick?.onClick(adapterPosition)
            }
            binding.tvTitle.setOnClickListener {
                itemClick?.onClick(adapterPosition)
            }
            binding.tvContent.setOnClickListener {
                itemClick?.onClick(adapterPosition)
            }
            binding.btnReply.setOnClickListener {
                itemClick?.onClick(adapterPosition)
            }
            binding.likeButton.setOnClickListener {
                itemClick?.onLikeClick(adapterPosition)
            }
            binding.bookmarkButton.setOnClickListener {
                itemClick?.onBookmarkClick(adapterPosition)
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

            bindMovieImage(item.imageUrl.toString())
        }

        fun bindMovieImage(imageUrl: String) {
            Glide.with(binding.root.context)
                .load(imageUrl)
                .into(binding.movieImage)
        }

        fun bindBookmarkButton(isBookmarked: Boolean) {
            if (isBookmarked) {
                binding.bookmarkButton.setImageResource(R.drawable.ic_bookmark_selected)
            } else {
                binding.bookmarkButton.setImageResource(R.drawable.ic_bookmark_unselected)
            }
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
