package com.teamfilmo.filmo.data.remote.model.user

import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(
    val uid: String,
    val type: String,
    val profileUid: String = "",
)
