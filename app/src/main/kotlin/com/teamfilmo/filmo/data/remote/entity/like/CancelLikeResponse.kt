package com.teamfilmo.filmo.data.remote.entity.like

import kotlinx.serialization.Serializable

@Serializable
data class CancelLikeResponse(
    val likeId: String,
)
