package com.teamfilmo.filmo.domain.follow

import com.teamfilmo.filmo.domain.repository.FollowRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class CheckIsFollowUseCase
    @Inject
    constructor(
        private val followRepository: FollowRepository,
    ) {
        operator fun invoke(targetId: String): Flow<Boolean?> =
            flow {
                val result = followRepository.checkIsFollow(targetId)
                result.onFailure {
                    throw it
                }
                emit(result.getOrNull())
            }.catch {
                Timber.d("failed to check isFollow : ${it.localizedMessage}")
            }
    }
