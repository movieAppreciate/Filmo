package com.teamfilmo.filmo.domain.auth

import com.teamfilmo.filmo.data.remote.model.user.SignUpRequest
import com.teamfilmo.filmo.data.remote.model.user.SignUpResponse
import com.teamfilmo.filmo.domain.repository.AuthRepository
import javax.inject.Inject

class SignUpUseCase
    @Inject
    constructor(
        private val authRepository: AuthRepository,
    ) {
        suspend operator fun invoke(signUpRequest: SignUpRequest): Result<SignUpResponse> = authRepository.signUp(signUpRequest)
    }
