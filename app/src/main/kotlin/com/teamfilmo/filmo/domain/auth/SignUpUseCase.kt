package com.teamfilmo.filmo.domain.auth

import com.teamfilmo.filmo.data.remote.model.user.SignUpRequest
import com.teamfilmo.filmo.data.remote.model.user.SignUpResponse
import com.teamfilmo.filmo.domain.repository.AuthRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SignUpUseCase
    @Inject
    constructor(
        private val authRepository: AuthRepository,
    ) {
        operator fun invoke(signUpRequest: SignUpRequest): Flow<SignUpResponse?> =
            flow {
                authRepository
                    .signUp(signUpRequest)
                    .onSuccess {
                        emit(it)
                    }.onFailure {
                        emit(null)
                    }
            }
    }
