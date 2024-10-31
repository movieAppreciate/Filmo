package com.teamfilmo.filmo.data.remote.model.follow

import kotlinx.serialization.Serializable

@Serializable
data class FollowerListResponse(
    val followerUserInfoList: List<FollowUserInfo>,
    val hasNext: Boolean,
)
