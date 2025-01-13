package com.teamfilmo.filmo.data.remote.entity.reply.get

import kotlinx.serialization.Serializable

@Serializable
data class GetReplyResponseItem(
    val content: String,
    val createDate: String,
    val lastModifiedDate: String,
    val nickname: String,
    val replyId: String,
    val reportId: String,
    val subReply: ArrayList<SubReplyResponse>? = null,
    val userId: String,
)
