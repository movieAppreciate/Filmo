package com.teamfilmo.filmo.domain.like

import com.teamfilmo.filmo.domain.repository.LikeRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class RegistLikeUseCase
    @Inject
    constructor(
        private val likeRepository: LikeRepository,
    ) {
        operator fun invoke(reportId: String): Flow<String?> =
            flow {
                val result =
                    likeRepository.registLike(reportId)
                        .onFailure {
                            throw it
                        }
                emit(result.getOrNull())
            }.catch {
                Timber.e(it)
            }
    }
