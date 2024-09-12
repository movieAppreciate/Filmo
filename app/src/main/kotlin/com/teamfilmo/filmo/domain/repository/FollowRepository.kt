package com.teamfilmo.filmo.domain.repository

import com.teamfilmo.filmo.data.remote.model.follow.FollowCountResponse

interface FollowRepository {
    suspend fun countFollow(
        otherUserId: String,
    ): Result<FollowCountResponse>
}
