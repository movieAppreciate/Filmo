package com.teamfilmo.filmo.data.remote.model.user

import kotlinx.serialization.Serializable

@Serializable
data class DeleteUserResponse(
    val success: Boolean = false,
)
