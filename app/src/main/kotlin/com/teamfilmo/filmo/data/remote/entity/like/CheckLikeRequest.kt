package com.teamfilmo.filmo.data.remote.entity.like

import kotlinx.serialization.Serializable

@Serializable
data class CheckLikeRequest(
    val targetId: String,
    val type: String,
)
