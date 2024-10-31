package com.teamfilmo.filmo.data.remote.model.follow

import kotlinx.serialization.Serializable

@Serializable
data class FollowingListResponse(
    val followingUserInfoList: List<FollowUserInfo>,
    val hasNext: Boolean,
)
