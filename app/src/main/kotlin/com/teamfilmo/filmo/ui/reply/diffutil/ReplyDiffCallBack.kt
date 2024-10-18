package com.teamfilmo.filmo.ui.reply.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.teamfilmo.filmo.data.remote.model.reply.get.GetReplyResponseItemWithRole

class ReplyDiffCallBack(
    private val oldList: List<GetReplyResponseItemWithRole>,
    private val newList: List<GetReplyResponseItemWithRole>,
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int,
    ): Boolean {
        return oldList[oldItemPosition].replyId == newList[newItemPosition].replyId
    }

    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int,
    ): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
