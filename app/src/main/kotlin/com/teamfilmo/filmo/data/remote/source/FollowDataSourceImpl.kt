package com.teamfilmo.filmo.data.remote.source

import com.teamfilmo.filmo.data.remote.model.follow.count.FollowCountResponse
import com.teamfilmo.filmo.data.remote.model.follow.save.SaveFollowResponse
import com.teamfilmo.filmo.data.remote.service.FollowService
import com.teamfilmo.filmo.data.source.FollowDataSource
import javax.inject.Inject

class FollowDataSourceImpl
    @Inject
    constructor(
        private val followService: FollowService,
    ) : FollowDataSource {
        override suspend fun checkIsFollow(targetId: String): Result<Boolean> {
            return followService.isFollow(targetId)
        }

        override suspend fun cancelFollow(followId: String): Result<Unit> {
            return followService.cancelFollow(followId)
        }

        override suspend fun saveFollow(saveFollowRequest: String): Result<SaveFollowResponse> {
            return followService.saveFollow(saveFollowRequest)
        }

        override suspend fun countFollow(otherUserId: String): Result<FollowCountResponse> {
            return followService.countFollow(otherUserId)
        }
    }
