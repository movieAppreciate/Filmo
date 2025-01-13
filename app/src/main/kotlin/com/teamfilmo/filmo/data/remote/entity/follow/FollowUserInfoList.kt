package com.teamfilmo.filmo.data.remote.entity.follow

import kotlinx.serialization.Serializable

@Serializable
data class FollowUserInfoList(
    val followUserInfoList: List<FollowUserInfo>,
    val hasNext: Boolean,
)
