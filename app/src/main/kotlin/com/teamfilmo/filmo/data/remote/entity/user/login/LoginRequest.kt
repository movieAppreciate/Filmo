package com.teamfilmo.filmo.data.remote.entity.user.login

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val type: String,
)
