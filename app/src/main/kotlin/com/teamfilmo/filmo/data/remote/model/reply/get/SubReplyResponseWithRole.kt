package com.teamfilmo.filmo.data.remote.model.reply.get

data class SubReplyResponseWithRole(
    val content: String,
    val createDate: String,
    val lastModifiedDate: String,
    val nickname: String,
    val replyId: String,
    val reportId: String,
    val upReplyId: String,
    val userId: String,
    val isMySubReply: Boolean,
)
