package com.teamfilmo.filmo.domain.like

import com.teamfilmo.filmo.domain.repository.LikeRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CheckLikeStateUseCase
    @Inject
    constructor(
        private val likeRepository: LikeRepository,
    ) {
        suspend operator fun invoke(
            targetId: String,
            type: String,
        ): Flow<Boolean> =
            flow {
                val result = likeRepository.checkLike(targetId, type)
                emit(result.getOrDefault(false))
            }
    }
