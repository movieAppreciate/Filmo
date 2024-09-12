package com.teamfilmo.filmo.data.repository

import com.teamfilmo.filmo.data.remote.model.follow.FollowCountResponse
import com.teamfilmo.filmo.data.source.FollowDataSource
import com.teamfilmo.filmo.domain.repository.FollowRepository
import javax.inject.Inject

class FollowRepositoryImpl
    @Inject
    constructor(
        private val followDataSource: FollowDataSource,
    ) : FollowRepository {
        override suspend fun countFollow(otherUserId: String): Result<FollowCountResponse> {
            return followDataSource.countFollow(otherUserId)
        }
    }
