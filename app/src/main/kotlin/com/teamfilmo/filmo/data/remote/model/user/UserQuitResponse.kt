package com.teamfilmo.filmo.data.remote.model.user

import kotlinx.serialization.Serializable

@Serializable
data class UserQuitResponse(
    val success: Boolean = false,
)
