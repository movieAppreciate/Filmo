package com.teamfilmo.filmo.domain.like

import com.teamfilmo.filmo.domain.repository.LikeRepository
import javax.inject.Inject

class RegistLikeUseCase
    @Inject
    constructor(
        private val likeRepository: LikeRepository,
    ) {
        suspend operator fun invoke(reportId: String): Result<String> {
            return likeRepository.registLike(reportId)
        }
    }
