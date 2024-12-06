package com.teamfilmo.filmo.data.remote.model.user

data class TokenInfo(
    val type: String,
    val accessToken: String,
    val refreshToken: String,
)
