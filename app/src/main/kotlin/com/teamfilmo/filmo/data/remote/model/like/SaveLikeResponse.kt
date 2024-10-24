package com.teamfilmo.filmo.data.remote.model.like

import kotlinx.serialization.Serializable

@Serializable
data class SaveLikeResponse(
    val likeId: String = "",
    val userId: String = "",
    val targetId: String = "",
    val type: String = "",
)
