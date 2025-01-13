package com.teamfilmo.filmo.data.remote.entity.follow

import kotlinx.serialization.Serializable

@Serializable
data class FollowUserInfo(
    val email: String? = null,
    val userId: String? = null,
    val type: String? = null,
    val nickname: String? = null,
    val profileUrl: String? = null,
    val lastLoginDate: String? = null,
    val introduction: String? = null,
    val roles: String? = null,
    val createDate: String? = null,
    val lastModifiedDate: String? = null,
)

@Serializable
data class MutualFollowUserInfo(
    val email: String? = null,
    val userId: String? = null,
    val type: String? = null,
    val nickname: String? = null,
    val profileUrl: String? = null,
    val lastLoginDate: String? = null,
    val introduction: String? = null,
    val roles: String? = null,
    val createDate: String? = null,
    val lastModifiedDate: String? = null,
    val isFollowing: Boolean? = null,
    val isMyFollowing: Boolean? = null,
)
