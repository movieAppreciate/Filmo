package com.teamfilmo.filmo.data.remote.entity.follow.check

import kotlinx.serialization.Serializable

@Serializable
data class CheckIsFollowResponse(
    val isFollowing: Boolean = false,
    val followId: String = "",
)
