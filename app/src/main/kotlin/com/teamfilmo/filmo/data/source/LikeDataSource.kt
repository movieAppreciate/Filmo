package com.teamfilmo.filmo.data.source

interface LikeDataSource {
    suspend fun registLike(reportId: String): Result<String>

    suspend fun checkLike(reportId: String): Result<Boolean>

    suspend fun cancelLike(reportId: String): Result<String>

    suspend fun countLike(reportId: String): Result<Int>
}
