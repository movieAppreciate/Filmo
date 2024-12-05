package com.teamfilmo.filmo.data.remote.model.like

import kotlinx.serialization.Serializable

@Serializable
data class CancelLikeResponse(
    val likeId: String,
)
