package com.teamfilmo.filmo.data.remote.entity.follow.save

import kotlinx.serialization.Serializable

@Serializable
data class SaveFollowResponse(
    val followId: String,
    // 팔로우할 사람의 userId
    val targetId: String,
    // 내 유저 아이디
    val userId: String,
)
