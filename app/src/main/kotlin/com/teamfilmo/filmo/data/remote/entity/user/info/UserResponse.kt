package com.teamfilmo.filmo.data.remote.entity.user.info

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val uid: String = "",
    val userId: String = "",
    val type: String = "",
    val nickname: String = "",
    val profileUrl: String? = null,
    val lastLoginDate: String = "",
    val roles: String = "",
    val createDate: String = "",
    val lastModifiedDate: String = "",
)
