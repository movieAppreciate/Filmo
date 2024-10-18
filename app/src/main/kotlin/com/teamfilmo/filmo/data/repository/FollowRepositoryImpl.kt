package com.teamfilmo.filmo.data.repository

import com.teamfilmo.filmo.data.remote.model.follow.count.FollowCountResponse
import com.teamfilmo.filmo.data.remote.model.follow.save.SaveFollowResponse
import com.teamfilmo.filmo.data.source.FollowDataSource
import com.teamfilmo.filmo.domain.repository.FollowRepository
import javax.inject.Inject

class FollowRepositoryImpl
    @Inject
    constructor(
        private val followDataSource: FollowDataSource,
    ) : FollowRepository {
        override suspend fun checkIsFollow(targetId: String): Result<Boolean> {
            return followDataSource.checkIsFollow(targetId)
        }

        override suspend fun cancelFollow(followId: String): Result<Unit> {
            return followDataSource.cancelFollow(followId)
        }

        override suspend fun saveFollow(saveFollowRequest: String): Result<SaveFollowResponse> {
            return followDataSource.saveFollow(saveFollowRequest)
        }

        override suspend fun countFollow(otherUserId: String): Result<FollowCountResponse> {
            return followDataSource.countFollow(otherUserId)
        }
    }
