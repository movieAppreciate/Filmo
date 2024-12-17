package com.teamfilmo.filmo.domain.follow

import com.teamfilmo.filmo.domain.repository.FollowRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import timber.log.Timber

class CancelFollowUseCase
    @Inject
    constructor(
        private val followRepository: FollowRepository,
    ) {
        operator fun invoke(followId: String): Flow<Boolean?> =
            flow {
                val result = followRepository.cancelFollow(followId)
                result.onFailure {
                    when (it) {
                        is HttpException -> Timber.e("Network error: ${it.message}")
                        else -> Timber.e("Unknown error: ${it.message}")
                    }
                    emit(null)
                }
                result.onSuccess {
                    emit(true)
                }
            }
    }
