package com.teamfilmo.filmo.domain.like

import com.teamfilmo.filmo.domain.repository.LikeRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class CountLikeUseCase
    @Inject
    constructor(
        private val likeRepository: LikeRepository,
    ) {
        operator fun invoke(reportId: String): Flow<Int> =
            flow {
                val result = likeRepository.countLike(reportId)
                emit(result.getOrDefault(0))
            }.catch {
                Timber.e(it)
            }
    }
