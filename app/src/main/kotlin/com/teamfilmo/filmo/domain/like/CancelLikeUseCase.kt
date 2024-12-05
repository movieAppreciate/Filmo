package com.teamfilmo.filmo.domain.like

import com.teamfilmo.filmo.data.remote.model.like.CancelLikeResponse
import com.teamfilmo.filmo.domain.repository.LikeRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import timber.log.Timber

class CancelLikeUseCase
    @Inject
    constructor(
        private val likeRepository: LikeRepository,
    ) {
        operator fun invoke(reportId: String): Flow<CancelLikeResponse?> =
            flow {
                val result = likeRepository.cancelLike(reportId)
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
