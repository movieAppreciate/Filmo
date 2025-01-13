package com.teamfilmo.filmo.data.remote.entity.reply.update

import kotlinx.serialization.Serializable

@Serializable
data class UpdateReplyResponse(
    val content: String,
    val lastModifiedDate: String,
    val replyId: String,
    val reportId: String,
    val userId: String,
)
