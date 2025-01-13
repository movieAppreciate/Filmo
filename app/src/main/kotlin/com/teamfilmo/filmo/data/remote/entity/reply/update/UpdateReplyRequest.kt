package com.teamfilmo.filmo.data.remote.entity.reply.update

import kotlinx.serialization.Serializable

@Serializable
data class UpdateReplyRequest(
    val content: String,
    val replyId: String,
)
