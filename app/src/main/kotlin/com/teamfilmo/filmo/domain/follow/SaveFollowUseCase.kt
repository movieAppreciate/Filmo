package com.teamfilmo.filmo.domain.follow

import com.teamfilmo.filmo.data.remote.entity.follow.save.SaveFollowResponse
import com.teamfilmo.filmo.domain.repository.FollowRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import timber.log.Timber

class SaveFollowUseCase
    @Inject
    constructor(
        private val followRepository: FollowRepository,
    ) {
        operator fun invoke(saveFollowRequest: String): Flow<SaveFollowResponse?> =
            flow {
                val result = followRepository.saveFollow(saveFollowRequest)
                result.onFailure {
                    when (it) {
                        is HttpException -> Timber.e("Network error: ${it.message}")
                        else -> Timber.e("Unknown error: ${it.message}")
                    }
                    emit(null)
                }
                result.onSuccess {
                    emit(result.getOrNull() ?: SaveFollowResponse(followId = "-1", userId = "-1", targetId = " -1)"))
                }
            }
    }
