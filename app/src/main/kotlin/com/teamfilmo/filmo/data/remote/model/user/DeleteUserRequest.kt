package com.teamfilmo.filmo.data.remote.model.user

import kotlinx.serialization.Serializable

@Serializable
data class DeleteUserRequest(
    private val userId: String,
)
