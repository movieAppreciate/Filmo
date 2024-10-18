package com.teamfilmo.filmo.data.remote.model.follow.check

import kotlinx.serialization.Serializable

@Serializable
data class CheckIsFollowResponse(
    val isFollow: Boolean,
//    val followId : String
)
