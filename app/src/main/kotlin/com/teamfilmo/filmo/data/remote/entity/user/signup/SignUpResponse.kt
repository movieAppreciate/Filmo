package com.teamfilmo.filmo.data.remote.entity.user.signup

import kotlinx.serialization.Serializable

@Serializable
data class SignUpResponse(
    val email: String,
    val userId: String,
    val type: String,
    val nickname: String,
    val profileUrl: String? = null,
    val lastLoginDate: String? = null,
    val introduction: String? = null,
    val roles: String,
    val createDate: String,
    val lastModifiedDate: String,
)
