package com.teamfilmo.filmo.domain.like

import com.teamfilmo.filmo.domain.repository.LikeRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class CheckLikeStateUseCase
    @Inject
    constructor(
        private val likeRepository: LikeRepository,
    ) {
        operator fun invoke(reportId: String): Flow<Boolean> =
            flow {
                val result = likeRepository.checkLike(reportId = reportId)
                emit(result.getOrNull() ?: false)
            }.catch {
                Timber.e(it)
            }
    }
