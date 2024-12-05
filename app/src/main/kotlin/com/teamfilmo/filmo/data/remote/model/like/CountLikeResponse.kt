package com.teamfilmo.filmo.data.remote.model.like

import kotlinx.serialization.Serializable

@Serializable
data class CountLikeResponse(
    val countLike: Int = 0,
)
