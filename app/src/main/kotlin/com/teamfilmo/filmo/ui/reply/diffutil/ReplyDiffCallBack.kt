package com.teamfilmo.filmo.ui.reply.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.teamfilmo.filmo.domain.model.reply.GetReplyItemWithRole

class ReplyDiffCallBack(
    private val oldList: List<GetReplyItemWithRole>,
    private val newList: List<GetReplyItemWithRole>,
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int,
    ): Boolean = oldList[oldItemPosition].replyId == newList[newItemPosition].replyId

    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int,
    ): Boolean = oldList[oldItemPosition] == newList[newItemPosition]
}
