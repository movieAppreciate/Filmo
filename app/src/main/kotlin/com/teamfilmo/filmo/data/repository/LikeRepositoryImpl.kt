package com.teamfilmo.filmo.data.repository

import com.teamfilmo.filmo.data.source.LikeDataSource
import com.teamfilmo.filmo.domain.repository.LikeRepository
import javax.inject.Inject

class LikeRepositoryImpl
    @Inject
    constructor(
        private val likeDataSource: LikeDataSource,
    ) : LikeRepository {
        override suspend fun registLike(reportId: String): Result<String> {
            return likeDataSource.registLike(reportId)
        }

        override suspend fun checkLike(reportId: String): Result<Boolean> {
            return likeDataSource.checkLike(reportId)
        }

        override suspend fun cancleLike(reportId: String): Result<String> {
            return likeDataSource.cancelLike(reportId)
        }

        override suspend fun countLike(reportId: String): Result<Int> {
            return likeDataSource.countLike(reportId)
        }
    }
