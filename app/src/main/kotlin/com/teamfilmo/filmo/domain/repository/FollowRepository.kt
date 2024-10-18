package com.teamfilmo.filmo.domain.repository

import com.teamfilmo.filmo.data.remote.model.follow.count.FollowCountResponse
import com.teamfilmo.filmo.data.remote.model.follow.save.SaveFollowResponse

interface FollowRepository {
    suspend fun checkIsFollow(
        targetId: String,
    ): Result<Boolean>

    suspend fun cancelFollow(
        followId: String,
    ): Result<Unit>

    suspend fun saveFollow(
        saveFollowRequest: String,
    ): Result<SaveFollowResponse>

    suspend fun countFollow(
        otherUserId: String,
    ): Result<FollowCountResponse>
}
