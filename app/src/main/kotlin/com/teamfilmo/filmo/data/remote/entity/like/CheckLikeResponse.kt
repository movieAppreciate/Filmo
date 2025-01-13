package com.teamfilmo.filmo.data.remote.entity.like

import kotlinx.serialization.Serializable

@Serializable
data class CheckLikeResponse(
    val likeId: String? = null,
    val isLike: Boolean = false,
)
