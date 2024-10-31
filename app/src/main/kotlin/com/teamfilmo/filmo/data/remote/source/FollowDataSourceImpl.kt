package com.teamfilmo.filmo.data.remote.source

import com.teamfilmo.filmo.data.remote.model.follow.FollowerListResponse
import com.teamfilmo.filmo.data.remote.model.follow.FollowingListResponse
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
        override suspend fun getFollowingList(
            userId: String?,
            lastUserId: String?,
            keyword: String?,
        ): Result<FollowingListResponse> = followService.getFollowingList(userId, lastUserId, keyword)

        override suspend fun getFollowerList(
            userId: String?,
            lastUserId: String?,
            keyword: String?,
        ): Result<FollowerListResponse> = followService.getFollowerList(userId, lastUserId, keyword)

        override suspend fun checkIsFollow(targetId: String): Result<Boolean> = followService.isFollow(targetId)

        override suspend fun cancelFollow(followId: String): Result<Unit> = followService.cancelFollow(followId)

        override suspend fun saveFollow(saveFollowRequest: String): Result<SaveFollowResponse> = followService.saveFollow(saveFollowRequest)

        override suspend fun countFollow(otherUserId: String?): Result<FollowCountResponse> = followService.countFollow(otherUserId)
    }
