package com.teamfilmo.filmo.data.remote.model.like

import kotlinx.serialization.Serializable

@Serializable
data class CheckLikeResponse(
    val likeId: String? = null,
    val isLike: Boolean = false,
)
