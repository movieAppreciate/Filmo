package com.teamfilmo.filmo.data.remote.model.follow.check

import kotlinx.serialization.Serializable

@Serializable
data class CheckIsFollowResponse(
    val isFollowing: Boolean,
//    val followId : String
)
