package com.teamfilmo.filmo.data.remote.entity.follow.count

import kotlinx.serialization.Serializable

@Serializable
data class FollowCountResponse(
    val countFollowing: Int = 0,
    val countFollower: Int = 0,
)
