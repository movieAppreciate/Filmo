package com.teamfilmo.filmo.data.remote.source

import com.teamfilmo.filmo.data.remote.model.follow.FollowCountResponse
import com.teamfilmo.filmo.data.remote.service.FollowService
import com.teamfilmo.filmo.data.source.FollowDataSource
import javax.inject.Inject

class FollowDataSourceImpl
    @Inject
    constructor(
        private val followService: FollowService,
    ) : FollowDataSource {
        override suspend fun countFollow(otherUserId: String): Result<FollowCountResponse> {
            return followService.countFollow(otherUserId)
        }
    }
