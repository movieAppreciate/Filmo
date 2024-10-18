package com.teamfilmo.filmo.domain.follow

import com.teamfilmo.filmo.data.remote.model.follow.save.SaveFollowResponse
import com.teamfilmo.filmo.domain.repository.FollowRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class SaveFollowUseCase
    @Inject
    constructor(
        private val followRepository: FollowRepository,
    ) {
        operator fun invoke(saveFollowRequest: String): Flow<SaveFollowResponse> =
            flow {
                val result = followRepository.saveFollow(saveFollowRequest)
                result.onFailure {
                    throw it
                }
                emit(result.getOrNull() ?: SaveFollowResponse(followId = "-1", userId = "-1", targetId = " -1)"))
            }.catch {
                Timber.d("failed to save follow : ${it.cause}")
            }
    }
