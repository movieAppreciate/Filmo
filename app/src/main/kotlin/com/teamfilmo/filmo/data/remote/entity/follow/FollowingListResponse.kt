package com.teamfilmo.filmo.data.remote.entity.follow

import kotlinx.serialization.Serializable

@Serializable
data class FollowingListResponse(
    val followingUserInfoList: List<FollowUserInfo>,
    val hasNext: Boolean,
)
