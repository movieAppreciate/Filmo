package com.teamfilmo.filmo.data.source

import com.teamfilmo.filmo.data.remote.model.like.CancelLikeResponse
import com.teamfilmo.filmo.data.remote.model.like.CheckLikeResponse
import com.teamfilmo.filmo.data.remote.model.like.CountLikeResponse
import com.teamfilmo.filmo.data.remote.model.like.SaveLikeRequest
import com.teamfilmo.filmo.data.remote.model.like.SaveLikeResponse

interface LikeDataSource {
    suspend fun saveLike(saveLikeRequest: SaveLikeRequest): Result<SaveLikeResponse>

    suspend fun checkLike(
        targetId: String,
        type: String,
    ): Result<CheckLikeResponse>

    suspend fun cancelLike(likeId: String): Result<CancelLikeResponse>

    suspend fun countLike(reportId: String): Result<CountLikeResponse>
}
