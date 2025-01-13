package com.teamfilmo.filmo.data.repository

import com.teamfilmo.filmo.data.remote.entity.follow.FollowerListResponse
import com.teamfilmo.filmo.data.remote.entity.follow.FollowingListResponse
import com.teamfilmo.filmo.data.remote.entity.follow.check.CheckIsFollowResponse
import com.teamfilmo.filmo.data.remote.entity.follow.count.FollowCountResponse
import com.teamfilmo.filmo.data.remote.entity.follow.save.SaveFollowResponse
import com.teamfilmo.filmo.data.source.FollowDataSource
import com.teamfilmo.filmo.domain.repository.FollowRepository
import javax.inject.Inject

class FollowRepositoryImpl
    @Inject
    constructor(
        private val followDataSource: FollowDataSource,
    ) : FollowRepository {
        override suspend fun getFollowerList(
            userId: String?,
            lastUserId: String?,
            keyword: String?,
        ): Result<FollowerListResponse> = followDataSource.getFollowerList(userId, lastUserId, keyword)

        override suspend fun getFollowingList(
            userId: String?,
            lastUserId: String?,
            keyword: String?,
        ): Result<FollowingListResponse> = followDataSource.getFollowingList(userId, lastUserId, keyword)

        override suspend fun checkIsFollow(targetId: String): Result<CheckIsFollowResponse> = followDataSource.checkIsFollow(targetId)

        override suspend fun cancelFollow(followId: String): Result<Unit> = followDataSource.cancelFollow(followId)

        override suspend fun saveFollow(saveFollowRequest: String): Result<SaveFollowResponse> = followDataSource.saveFollow(saveFollowRequest)

        override suspend fun countFollow(otherUserId: String?): Result<FollowCountResponse> = followDataSource.countFollow(otherUserId)
    }
