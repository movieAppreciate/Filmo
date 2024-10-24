package com.teamfilmo.filmo.data.remote.model.user

import kotlinx.serialization.Serializable

@Serializable
data class LoginFailureResponse(
    val timeStamp: String,
    val status: Int,
    val error: String,
    val message: String,
    val path: String,
)
