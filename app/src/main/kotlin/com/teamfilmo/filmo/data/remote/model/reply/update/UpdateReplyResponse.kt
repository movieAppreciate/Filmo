package com.teamfilmo.filmo.data.remote.model.reply.update

data class UpdateReplyResponse(
    val content: String,
    val lastModifiedDate: String,
    val replyId: String,
    val reportId: String,
    val userId: String,
)
