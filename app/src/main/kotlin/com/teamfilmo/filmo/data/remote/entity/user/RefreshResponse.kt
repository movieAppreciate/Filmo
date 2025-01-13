package com.teamfilmo.filmo.data.remote.entity.user

import kotlinx.serialization.Serializable

@Serializable
data class RefreshResponse(
    val accessToken: String,
    val refreshToken: String,
)
