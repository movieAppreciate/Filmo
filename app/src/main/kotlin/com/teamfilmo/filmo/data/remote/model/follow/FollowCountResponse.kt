package com.teamfilmo.filmo.data.remote.model.follow

import kotlinx.serialization.Serializable

@Serializable
data class FollowCountResponse(
    val countFollowing: Int,
    val countFollower: Int,
)
