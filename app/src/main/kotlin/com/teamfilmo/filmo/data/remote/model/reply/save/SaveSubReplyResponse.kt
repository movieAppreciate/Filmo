package com.teamfilmo.filmo.data.remote.model.reply.save

import kotlinx.serialization.Serializable

@Serializable
data class SaveSubReplyResponse(
    val replyId: String,
    val upReplyId: String,
    val reportId: String,
    val content: String,
    val userId: String,
)
