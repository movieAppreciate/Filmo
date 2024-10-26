package com.teamfilmo.filmo.data.source

import com.teamfilmo.filmo.data.remote.model.like.SaveLikeRequest
import com.teamfilmo.filmo.data.remote.model.like.SaveLikeResponse

interface LikeDataSource {
    suspend fun saveLike(saveLikeRequest: SaveLikeRequest): Result<SaveLikeResponse>

    suspend fun checkLike(
        targetId: String,
        type: String,
    ): Result<Boolean>

    suspend fun cancelLike(likeId: String): Result<String>

    suspend fun countLike(reportId: String): Result<Int>
}
