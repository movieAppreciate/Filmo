package com.teamfilmo.filmo.domain.auth

import com.teamfilmo.filmo.data.remote.entity.user.signup.SignUpRequest
import com.teamfilmo.filmo.data.remote.entity.user.signup.SignUpResult
import com.teamfilmo.filmo.domain.repository.AuthRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SignUpUseCase
    @Inject
    constructor(
        private val authRepository: AuthRepository,
    ) {
        operator fun invoke(signUpRequest: SignUpRequest): Flow<SignUpResult?> =
            flow {
                authRepository
                    .signUp(signUpRequest)
                    .onSuccess {
                        when (it) {
                            is SignUpResult.Success -> {
                                emit(SignUpResult.Success(it.response))
                            }
                            is SignUpResult.Existing -> {
                                emit(SignUpResult.Existing(it.response))
                            }
                            else -> {}
                        }
                    }.onFailure {
                        emit(SignUpResult.Error("회원 가입 중 오류가 발생했습니다 , ${it.localizedMessage}"))
                    }
            }
    }
