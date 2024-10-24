package com.teamfilmo.filmo.domain.like

import com.teamfilmo.filmo.data.remote.model.like.SaveLikeRequest
import com.teamfilmo.filmo.data.remote.model.like.SaveLikeResponse
import com.teamfilmo.filmo.domain.repository.LikeRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class SaveLikeUseCase
    @Inject
    constructor(
        private val likeRepository: LikeRepository,
    ) {
        operator fun invoke(saveLikeRequest: SaveLikeRequest): Flow<SaveLikeResponse> =
            flow {
                val result = likeRepository.saveLike(saveLikeRequest)
                result.onFailure {
                    throw it
                }
                emit(result.getOrNull() ?: SaveLikeResponse())
            }.catch {
                Timber.i("failed to save like : ${it.localizedMessage}")
            }
    }
