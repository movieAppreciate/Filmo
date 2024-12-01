package com.teamfilmo.filmo.data.remote.model.user

import kotlinx.serialization.Serializable

@Serializable
data class UserQuitRequest(
    private val userId: String,
)
