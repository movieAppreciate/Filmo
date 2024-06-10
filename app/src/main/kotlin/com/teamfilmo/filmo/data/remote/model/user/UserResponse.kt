package com.teamfilmo.filmo.data.remote.model.user

import kotlinx.serialization.Serializable

@Serializable
open class UserResponse(
    open val uid: String,
    open val userId: String,
    open val nickname: String,
    open val type: String,
    open val profileUrl: String? = null,
    open val lastLoginDate: String,
    open val introduction: String,
    open val role: List<String> = emptyList(),
)
