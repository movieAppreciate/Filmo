package com.teamfilmo.filmo.domain.follow

import com.teamfilmo.filmo.domain.repository.FollowRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class CancelFollowUseCase
    @Inject
    constructor(
        private val followRepository: FollowRepository,
    ) {
        operator fun invoke(followId: String): Flow<Boolean> =
            flow {
                val result = followRepository.cancelFollow(followId)

                result.onFailure {
                    throw it
                }
                result.onSuccess {
                    emit(true)
                }
            }.catch {
                Timber.e("failed to cancel follow : ${it.localizedMessage}")
            }
    }
