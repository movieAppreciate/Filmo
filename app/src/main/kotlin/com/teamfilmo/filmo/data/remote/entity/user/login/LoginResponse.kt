package com.teamfilmo.filmo.data.remote.entity.user.login

import kotlinx.serialization.Serializable

// sealed class LoginResponse {
@Serializable
data class LoginResponse(
    val grantType: String,
    val accessToken: String,
    val refreshToken: String,
    val userId: String,
)
