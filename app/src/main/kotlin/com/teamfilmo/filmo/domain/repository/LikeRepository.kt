package com.teamfilmo.filmo.domain.repository

import com.teamfilmo.filmo.data.remote.model.like.SaveLikeRequest
import com.teamfilmo.filmo.data.remote.model.like.SaveLikeResponse

interface LikeRepository {
    suspend fun saveLike(saveLikeRequest: SaveLikeRequest): Result<SaveLikeResponse>

    suspend fun checkLike(
        targetId: String,
        type: String,
    ): Result<Boolean>

    suspend fun cancleLike(reportId: String): Result<String>

    suspend fun countLike(reportId: String): Result<Int>
}
