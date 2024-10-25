package com.teamfilmo.filmo.domain.auth

import com.teamfilmo.filmo.data.remote.model.user.LoginRequest
import com.teamfilmo.filmo.data.remote.model.user.LoginResponse
import com.teamfilmo.filmo.domain.repository.AuthRepository
import javax.inject.Inject

class NaverLoginRequestUseCase
    @Inject
    constructor(
        private val authRepository: AuthRepository,
    ) {
        suspend operator fun invoke(email: String): Result<LoginResponse> {
            return authRepository.login(
                LoginRequest(email, AuthType.NAVER.value),
            )
        }
    }
