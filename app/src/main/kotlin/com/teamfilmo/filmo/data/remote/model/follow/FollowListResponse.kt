package com.teamfilmo.filmo.data.remote.model.follow

import com.teamfilmo.filmo.data.remote.model.follow.save.SaveFollowResponse
import kotlinx.serialization.Serializable

@Serializable
data class FollowListResponse(
    val followingUserInfoList: List<SaveFollowResponse>,
    val hasNext: Boolean,
)
