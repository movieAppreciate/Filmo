package com.teamfilmo.filmo.domain.repository

import com.teamfilmo.filmo.data.remote.entity.like.CancelLikeResponse
import com.teamfilmo.filmo.data.remote.entity.like.CheckLikeResponse
import com.teamfilmo.filmo.data.remote.entity.like.CountLikeResponse
import com.teamfilmo.filmo.data.remote.entity.like.SaveLikeRequest
import com.teamfilmo.filmo.data.remote.entity.like.SaveLikeResponse

interface LikeRepository {
    suspend fun saveLike(saveLikeRequest: SaveLikeRequest): Result<SaveLikeResponse>

    suspend fun checkLike(
        targetId: String,
        type: String,
    ): Result<CheckLikeResponse>

    suspend fun cancelLike(likeId: String): Result<CancelLikeResponse>

    suspend fun countLike(targetId: String): Result<CountLikeResponse>
}
