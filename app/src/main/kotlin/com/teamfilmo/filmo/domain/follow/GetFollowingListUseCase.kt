package com.teamfilmo.filmo.domain.follow

import com.teamfilmo.filmo.data.remote.model.follow.FollowingListResponse
import com.teamfilmo.filmo.domain.repository.FollowRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import timber.log.Timber

class GetFollowingListUseCase
    @Inject
    constructor(
        private val followRepository: FollowRepository,
    ) {
        operator fun invoke(
            userId: String,
            lastReportId: String? = null,
        ): Flow<FollowingListResponse?> =
            flow {
                val result = followRepository.getFollowingList(userId)
                result.onFailure {
                    when (it) {
                        is HttpException -> Timber.e("Network error: ${it.message}")
                        else -> Timber.e("Unknown error: ${it.message}")
                    }
                    emit(null)
                }

                // todo : 여기서 getOrThrow와 getOrNull 중 어떤 것으로 처리하는 것이 좋을까?
                emit(result.getOrNull())
            }
    }
