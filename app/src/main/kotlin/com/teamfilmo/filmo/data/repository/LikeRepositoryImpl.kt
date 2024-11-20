package com.teamfilmo.filmo.data.repository

import com.teamfilmo.filmo.data.remote.model.like.CheckLikeResponse
import com.teamfilmo.filmo.data.remote.model.like.SaveLikeRequest
import com.teamfilmo.filmo.data.remote.model.like.SaveLikeResponse
import com.teamfilmo.filmo.data.source.LikeDataSource
import com.teamfilmo.filmo.domain.repository.LikeRepository
import javax.inject.Inject

class LikeRepositoryImpl
    @Inject
    constructor(
        private val likeDataSource: LikeDataSource,
    ) : LikeRepository {
        override suspend fun saveLike(saveLikeRequest: SaveLikeRequest): Result<SaveLikeResponse> = likeDataSource.saveLike(saveLikeRequest)

        override suspend fun checkLike(
            targetId: String,
            type: String,
        ): Result<CheckLikeResponse> = likeDataSource.checkLike(targetId, type)

        override suspend fun cancelLike(likeId: String): Result<String> = likeDataSource.cancelLike(likeId)

        override suspend fun countLike(targetId: String): Result<Int> = likeDataSource.countLike(targetId)
    }
