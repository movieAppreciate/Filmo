package com.teamfilmo.filmo.data.remote.model.reply.get

import kotlinx.serialization.Serializable

@Serializable
data class GetReplyResponse(
    val replyList: List<GetReplyResponseItem>,
)
