package com.teamfilmo.filmo.ui.report.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamfilmo.filmo.R
import com.teamfilmo.filmo.data.remote.model.bookmark.BookmarkResponse
import com.teamfilmo.filmo.databinding.MovieItemBinding
import com.teamfilmo.filmo.model.report.ReportItem

sealed class ReportPayload {
    data class BookmarkPayload(var isBookmarked: Boolean) : ReportPayload()

    data class LikePayload(val isLiked: Boolean) : ReportPayload()

    data class LikeCountPayload(val likeCount: Int) : ReportPayload()
}

class AllMovieReportAdapter : RecyclerView.Adapter<AllMovieReportAdapter.AllMovieReportViewHolder>() {
    var bookmarkList: List<BookmarkResponse> = mutableListOf()
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

    fun setBookmark(bookmarkList: List<BookmarkResponse>) {
        this.bookmarkList = bookmarkList
        notifyItemRangeChanged(0, this.bookmarkList.size)
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

        val isBookmarked =
            bookmarkList.any {
                it.reportId == reportList[position].reportId
            }

        holder.bindBookmarkButton(isBookmarked)
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
                        Log.d("어댑터", "북마크 payload ${payload.isBookmarked}")
                        holder.bindBookmarkButton(payload.isBookmarked)
                    }

                    is ReportPayload.LikePayload -> {
                        Log.d("어댑터", "좋아요 payload ${payload.isLiked}")
                        this.reportList[position].isLiked = payload.isLiked
                        holder.bindLikeButton(if (payload.isLiked) R.drawable.ic_like_selected else R.drawable.ic_like_unselected)
                    }

                    is ReportPayload.LikeCountPayload -> {
                        this.reportList[position].likeCount = payload.likeCount
                        holder.bindLikeCount(payload.likeCount)
                    }

                    else -> {
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
        }

        fun bindBookmarkButton(isBookmarked: Boolean) {
            Log.d("북마크 바인딩", isBookmarked.toString())
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
            Log.d("좋아요 어댑터", isLiked.toString())
            if (isLiked) {
                binding.likeButton.setImageResource(R.drawable.ic_like_selected)
            } else {
                binding.likeButton.setImageResource(R.drawable.ic_like_unselected)
            }
        }
    }

    /*
    감상문 필터를 위함
     */
}
