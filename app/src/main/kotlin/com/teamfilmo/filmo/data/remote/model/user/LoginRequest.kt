package com.teamfilmo.filmo.data.remote.model.user

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val uid: String,
    val type: String,
)
