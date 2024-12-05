package com.teamfilmo.filmo.data.remote.source

import com.teamfilmo.filmo.data.remote.model.like.CancelLikeResponse
import com.teamfilmo.filmo.data.remote.model.like.CheckLikeResponse
import com.teamfilmo.filmo.data.remote.model.like.CountLikeResponse
import com.teamfilmo.filmo.data.remote.model.like.SaveLikeRequest
import com.teamfilmo.filmo.data.remote.model.like.SaveLikeResponse
import com.teamfilmo.filmo.data.remote.service.LikeService
import com.teamfilmo.filmo.data.source.LikeDataSource
import javax.inject.Inject

class LikeDataSourceImpl
    @Inject
    constructor(
        private val likeService: LikeService,
    ) : LikeDataSource {
        override suspend fun saveLike(saveLikeRequest: SaveLikeRequest): Result<SaveLikeResponse> = likeService.saveLike(saveLikeRequest)

        override suspend fun checkLike(
            targetId: String,
            type: String,
        ): Result<CheckLikeResponse> = likeService.checkLike(targetId, type)

        override suspend fun cancelLike(reportId: String): Result<CancelLikeResponse> = likeService.cancel(reportId)

        override suspend fun countLike(targetId: String): Result<CountLikeResponse> = likeService.count(targetId)
    }
