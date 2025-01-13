package com.teamfilmo.filmo.data.remote.entity.user.signup

import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(
    val email: String,
    val type: String,
    val profileUid: String? = null,
)
