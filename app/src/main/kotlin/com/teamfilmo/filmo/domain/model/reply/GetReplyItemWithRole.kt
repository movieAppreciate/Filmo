package com.teamfilmo.filmo.domain.model.reply

import com.teamfilmo.filmo.ui.reply.SubReplyWithLikeInfo

data class GetReplyItemWithRole(
    val content: String,
    val createDate: String,
    val lastModifiedDate: String,
    val nickname: String,
    val replyId: String,
    val reportId: String,
    val subReply: List<SubReplyWithLikeInfo>? = null,
    val userId: String,
    var isMyReply: Boolean,
    val isLiked: Boolean = false,
    val likeCount: Int,
)
