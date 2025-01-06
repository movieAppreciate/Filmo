package com.teamfilmo.filmo.data.remote.entity.user

import kotlinx.serialization.Serializable

@Serializable
data class ExistingUserResponse(
    val msg: String,
    val email: String,
    val type: String,
    val dup: String,
)
