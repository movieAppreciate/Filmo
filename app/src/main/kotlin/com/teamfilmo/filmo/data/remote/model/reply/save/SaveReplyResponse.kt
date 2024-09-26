package com.teamfilmo.filmo.data.remote.model.reply.save

import kotlinx.serialization.Serializable

@Serializable
data class SaveReplyResponse(
    val replyId: String,
    val reportId: String,
    val content: String,
    val userId: String,
)
