package com.teamfilmo.filmo.domain.auth

import com.teamfilmo.filmo.domain.repository.AuthRepository
import javax.inject.Inject

class KakaoLoginRequestUseCase
    @Inject
    constructor(
        private val authRepository: AuthRepository,
    ) {
        suspend operator fun invoke(email: String): Result<String> {
            return authRepository.login(email, AuthType.KAKAO.value)
        }
    }
