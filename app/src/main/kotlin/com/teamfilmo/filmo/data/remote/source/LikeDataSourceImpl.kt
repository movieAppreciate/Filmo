package com.teamfilmo.filmo.data.remote.source

import com.teamfilmo.filmo.data.remote.service.LikeService
import com.teamfilmo.filmo.data.source.LikeDataSource
import javax.inject.Inject

class LikeDataSourceImpl
    @Inject
    constructor(
        private val likeService: LikeService,
    ) : LikeDataSource {
        override suspend fun registLike(reportId: String): Result<String> {
            return likeService.regist(reportId)
        }

        override suspend fun checkLike(reportId: String): Result<Boolean> {
            return likeService.check(reportId)
        }

        override suspend fun cancelLike(reportId: String): Result<String> {
            return likeService.cancel(reportId)
        }

        override suspend fun countLike(reportId: String): Result<Int> {
            return likeService.count(reportId)
        }
    }
