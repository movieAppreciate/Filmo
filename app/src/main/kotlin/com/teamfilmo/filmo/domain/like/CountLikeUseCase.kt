package com.teamfilmo.filmo.domain.like

import com.teamfilmo.filmo.data.remote.model.like.CountLikeResponse
import com.teamfilmo.filmo.domain.repository.LikeRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import timber.log.Timber

class CountLikeUseCase
    @Inject
    constructor(
        private val likeRepository: LikeRepository,
    ) {
        operator fun invoke(targetId: String): Flow<CountLikeResponse?> =
            flow {
                val result = likeRepository.countLike(targetId)
                result.onSuccess {
                    emit(it)
                }
                result.onFailure {
                    when (it) {
                        // todo : 이후 UiState를 통해 HttpError 발생 시 에러 메시지 보이기
                        is HttpException -> Timber.e("Network error: ${it.message}")
                        else -> Timber.e("Unknown error: ${it.message}")
                    }
                    emit(null)
                }
                result.onSuccess {
                    emit(it)
                }
            }
    }
