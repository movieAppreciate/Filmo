package com.teamfilmo.filmo.ui.user.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamfilmo.filmo.data.remote.model.report.MyPageReportItem
import com.teamfilmo.filmo.databinding.ItemUserReportBinding

class UserPageAdapter : RecyclerView.Adapter<UserPageAdapter.MyPageViewHolder>() {
    private var myReportList: List<MyPageReportItem> = emptyList()

    // DiffUtil 적용하기
    fun setMyReportList(reportList: List<MyPageReportItem>) {
        this.myReportList = reportList
        notifyDataSetChanged()
    }

    inner class MyPageViewHolder(
        private val binding: ItemUserReportBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.txtMovieName.text = myReportList[position].movieName
            binding.txtReportContent.text = myReportList[position].reportContent
            binding.txtReportTitle.text = myReportList[position].reportTitle
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MyPageViewHolder {
        val binding = ItemUserReportBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyPageViewHolder(binding)
    }

    override fun getItemCount(): Int = myReportList.size

    override fun onBindViewHolder(
        holder: MyPageViewHolder,
        position: Int,
    ) {
        holder.bind(position)
    }
}
