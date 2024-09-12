package com.teamfilmo.filmo.domain.follow

import com.teamfilmo.filmo.data.remote.model.follow.FollowCountResponse
import com.teamfilmo.filmo.domain.repository.FollowRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class CountFollowUseCase
    @Inject
    constructor(
        private val followRepository: FollowRepository,
    ) {
        operator fun invoke(otherUserId: String): Flow<FollowCountResponse?> =
            flow {
                val result = followRepository.countFollow(otherUserId)
                result.onFailure {
                    throw it
                }
                emit(result.getOrNull())
            }.catch {
                Timber.d(it.message.toString())
            }
    }
