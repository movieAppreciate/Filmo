package com.teamfilmo.filmo.ui.report.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.teamfilmo.filmo.databinding.RecommendReportItemBinding
import com.teamfilmo.filmo.ui.model.bookmark.BookmarkResponse
import com.teamfilmo.filmo.ui.model.report.Report

class RecommendReportAdapter : RecyclerView.Adapter<RecommendReportAdapter.RecommendViewHolder>() {
    private var recommendList: List<Report> = listOf()
    private var bookmarkList: List<BookmarkResponse> = listOf()

    fun setRecommendReport(
        recommendList: List<Report>,
    ) {
        this.recommendList = recommendList
        notifyDataSetChanged()
    }

    fun setRecommendBookmark(bookmarkList: List<BookmarkResponse>) {
        this.bookmarkList = bookmarkList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecommendViewHolder {
        val binding = RecommendReportItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecommendViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(
        holder: RecommendViewHolder,
        position: Int,
    ) {
        holder.bindItems(item = recommendList[position])
    }

    inner class RecommendViewHolder(private val binding: RecommendReportItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val likeButton: ImageButton = binding.likeButton
        private val bookmarkButton: ImageButton = binding.bookmarkButton
        private var itemClick: ReportAdapter.ItemClick? = null

        init {
            likeButton.setOnClickListener {
                itemClick?.onLikeClick(adapterPosition)
            }
            bookmarkButton.setOnClickListener {
                itemClick?.onBookmarkClick(adapterPosition)
            }
        }

        fun bindItems(item: Report) {
            val title = binding.tvTitle
            val replyCount = binding.tvReplyCount
            val likeCount = binding.tvLikeCount
            title.text = item.title
            replyCount.text = item.replyCount.toString()
            likeCount.text = item.likeCount.toString()
        }
    }
}
