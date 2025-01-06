package com.teamfilmo.filmo.domain.like

import com.teamfilmo.filmo.data.remote.entity.like.CheckLikeResponse
import com.teamfilmo.filmo.domain.repository.LikeRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
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
                result.onSuccess {
                    emit(it)
                }
                result.onFailure {
                    when (it) {
                        is HttpException -> Timber.e("Network error: ${it.message}")
                        else -> Timber.e("Unknown error: ${it.message}")
                    }
                    emit(null)
                }
            }
    }
