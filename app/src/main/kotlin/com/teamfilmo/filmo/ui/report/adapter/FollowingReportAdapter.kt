package com.teamfilmo.filmo.ui.report.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamfilmo.filmo.databinding.FollowingReportItemBinding
import com.teamfilmo.filmo.model.report.ReportItem

class FollowingReportAdapter() : RecyclerView.Adapter<FollowingReportAdapter.FollowingReportViewHolder>() {
    private lateinit var followingReportList: List<ReportItem>

    fun setFollowingReportList(reportList: List<ReportItem>) {
        this.followingReportList = reportList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): FollowingReportViewHolder {
        val binding =
            FollowingReportItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return FollowingReportViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return followingReportList.size
    }

    override fun onBindViewHolder(
        holder: FollowingReportViewHolder,
        position: Int,
    ) {
        holder.bindItems(followingReportList[position])
    }

    override fun onBindViewHolder(
        holder: FollowingReportViewHolder,
        position: Int,
        payloads: MutableList<Any>,
    ) {
        super.onBindViewHolder(holder, position, payloads)
    }

    // 레이아웃과 데이터를 연결
    inner class FollowingReportViewHolder(private val binding: FollowingReportItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(items: ReportItem) {
            val title = binding.tvTitle
            val content = binding.tvContent
            val likeCount = binding.tvLikeCount
            val commentCount = binding.tvCommentCount
            val bookmarkCount = binding.tvBookmarkCount
            val nickname = binding.tvUserName

            title.text = items.title
            content.text = items.content
            likeCount.text = items.likeCount.toString()
            commentCount.text = items.replyCount.toString()
            bookmarkCount.text = items.bookmarkCount.toString()
            nickname.text = items.nickname
        }
    }
}
