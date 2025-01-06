package com.teamfilmo.filmo.data.remote.entity.user.delete

import kotlinx.serialization.Serializable

@Serializable
data class DeleteUserResponse(
    val success: Boolean = false,
)
