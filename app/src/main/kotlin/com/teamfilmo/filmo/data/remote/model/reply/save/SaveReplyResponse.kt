package com.teamfilmo.filmo.data.remote.model.reply.save

import kotlinx.serialization.Serializable

@Serializable
data class SaveReplyResponse(
    val upReplyId: String? = null,
    val replyId: String? = null,
    val reportId: String? = null,
    val content: String? = null,
    val userId: String? = null,
)
