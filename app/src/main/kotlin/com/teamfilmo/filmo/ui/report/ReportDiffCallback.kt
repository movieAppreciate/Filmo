package com.teamfilmo.filmo.ui.report

import androidx.recyclerview.widget.DiffUtil
import com.teamfilmo.filmo.data.remote.model.report.all.ReportItem

class ReportDiffCallback(
    private val oldList: List<ReportItem>,
    private val newList: List<ReportItem>,
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int,
    ): Boolean {
        return oldList[oldItemPosition].reportId == newList[newItemPosition].reportId
    }

    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int,
    ): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}