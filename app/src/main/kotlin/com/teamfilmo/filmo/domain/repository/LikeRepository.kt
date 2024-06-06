package com.teamfilmo.filmo.domain.repository

interface LikeRepository {
    suspend fun registLike(reportId: String): Result<String>

    suspend fun checkLike(reportId: String): Result<Boolean>

    suspend fun cancleLike(reportId: String): Result<String>

    suspend fun countLike(reportId: String): Result<Int>
}
