package com.teamfilmo.filmo.data.source

import com.teamfilmo.filmo.data.remote.model.follow.FollowCountResponse

interface FollowDataSource {
    suspend fun countFollow(
        otherUserId: String,
    ): Result<FollowCountResponse>
}
