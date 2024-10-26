package com.teamfilmo.filmo.domain.like

import com.teamfilmo.filmo.domain.repository.LikeRepository
import javax.inject.Inject
import timber.log.Timber

class CancelLikeUseCase
    @Inject
    constructor(
        private val likeRepository: LikeRepository,
    ) {
        suspend operator fun invoke(reportId: String): Result<String?> =
            likeRepository
                .cancelLike(reportId)
                .onSuccess {
                    Timber.d("success to cancel like")
                }.onFailure {
                    Timber.d("failed to cancel like : ${it.message}")
                }
    }
