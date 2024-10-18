package com.teamfilmo.filmo.data.remote.model.reply.get

data class GetReplyResponseItemWithRole(
    val content: String,
    val createDate: String,
    val lastModifiedDate: String,
    val nickname: String,
    val replyId: String,
    val reportId: String,
    val subReply: ArrayList<SubReplyResponse>? = null,
    val userId: String,
    var isMyReply: Boolean,
)
