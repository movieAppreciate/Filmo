package com.teamfilmo.filmo.data.remote.model.reply

import kotlinx.serialization.Serializable

@Serializable
data class DeleteReplyResponse(
    val success: Boolean,
)
