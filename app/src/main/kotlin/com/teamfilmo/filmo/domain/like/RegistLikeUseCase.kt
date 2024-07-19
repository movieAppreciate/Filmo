package com.teamfilmo.filmo.domain.like

import com.teamfilmo.filmo.domain.repository.LikeRepository
import javax.inject.Inject
import timber.log.Timber

class RegistLikeUseCase
    @Inject
    constructor(
        private val likeRepository: LikeRepository,
    ) {
        suspend operator fun invoke(reportId: String): Result<Unit> =
            likeRepository.registLike(reportId)
                .onSuccess { Timber.d("좋아요 등록 성공") }
                .onFailure { Timber.d("등록 실패: $it") }
    }
