package com.teamfilmo.filmo.domain.auth

import com.teamfilmo.filmo.data.remote.entity.user.login.LoginRequest
import com.teamfilmo.filmo.data.remote.entity.user.login.LoginResponse
import com.teamfilmo.filmo.domain.repository.AuthRepository
import javax.inject.Inject

class KakaoLoginRequestUseCase
    @Inject
    constructor(
        private val authRepository: AuthRepository,
    ) {
        suspend operator fun invoke(email: String): Result<LoginResponse> =
            authRepository.login(
                LoginRequest(
                    email,
                    AuthType.KAKAO.value,
                ),
            )
    }
