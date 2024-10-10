package com.teamfilmo.filmo.data.remote.model.user

import kotlinx.serialization.Serializable

@Serializable
data class LoginFailureResponse(
    val message: String,
    val status: String,
)
