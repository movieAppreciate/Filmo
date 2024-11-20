package com.teamfilmo.filmo.domain.like

import com.teamfilmo.filmo.data.remote.model.like.CheckLikeResponse
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
        operator fun invoke(
            targetId: String,
            type: String = "reply",
        ): Flow<CheckLikeResponse?> =
            flow {
                val result = likeRepository.checkLike(targetId, type)
                result.onFailure {
                    throw it
                }
                emit(result.getOrNull())
            }.catch { Timber.d("failed to check reply like state : ${it.localizedMessage}") }
    }
