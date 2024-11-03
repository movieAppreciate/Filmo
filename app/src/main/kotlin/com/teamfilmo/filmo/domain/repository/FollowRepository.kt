package com.teamfilmo.filmo.domain.repository

import com.teamfilmo.filmo.data.remote.model.follow.FollowerListResponse
import com.teamfilmo.filmo.data.remote.model.follow.FollowingListResponse
import com.teamfilmo.filmo.data.remote.model.follow.check.CheckIsFollowResponse
import com.teamfilmo.filmo.data.remote.model.follow.count.FollowCountResponse
import com.teamfilmo.filmo.data.remote.model.follow.save.SaveFollowResponse

interface FollowRepository {
    suspend fun getFollowerList(
        userId: String? = null,
        lastUserId: String? = null,
        keyword: String? = null,
    ): Result<FollowerListResponse>

    suspend fun getFollowingList(
        userId: String? = null,
        lastUserId: String? = null,
        keyword: String? = null,
    ): Result<FollowingListResponse>

    suspend fun checkIsFollow(
        targetId: String,
    ): Result<CheckIsFollowResponse>

    suspend fun cancelFollow(
        followId: String,
    ): Result<Unit>

    suspend fun saveFollow(
        saveFollowRequest: String,
    ): Result<SaveFollowResponse>

    suspend fun countFollow(
        otherUserId: String?,
    ): Result<FollowCountResponse>
}
