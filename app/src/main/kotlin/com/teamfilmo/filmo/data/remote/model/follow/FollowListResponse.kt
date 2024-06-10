package com.teamfilmo.filmo.data.remote.model.follow

import kotlinx.serialization.Serializable

@Serializable
data class FollowListResponse(
    val followingUserInfoList: List<FollowResponse>,
    val hasNext: Boolean,
)
