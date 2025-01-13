package com.teamfilmo.filmo.domain.follow

import com.teamfilmo.filmo.data.remote.entity.follow.check.CheckIsFollowResponse
import com.teamfilmo.filmo.domain.repository.FollowRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import timber.log.Timber

class CheckIsFollowUseCase
    @Inject
    constructor(
        private val followRepository: FollowRepository,
    ) {
        operator fun invoke(targetId: String): Flow<CheckIsFollowResponse?> =
            flow {
                val result = followRepository.checkIsFollow(targetId)
                result.onFailure {
                    when (it) {
                        is HttpException -> Timber.e("Network error: ${it.message}")
                        else -> Timber.e("Unknown error: ${it.message}")
                    }
                    emit(null)
                }
                emit(result.getOrNull())
            }
    }
