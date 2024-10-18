package com.teamfilmo.filmo.data.remote.model.follow.count

import kotlinx.serialization.Serializable

@Serializable
data class FollowCountResponse(
    val countFollowing: Int,
    val countFollower: Int,
)
