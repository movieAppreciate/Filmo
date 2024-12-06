package com.teamfilmo.filmo.data.remote.source

import com.teamfilmo.filmo.data.remote.model.user.LoginRequest
import com.teamfilmo.filmo.data.remote.model.user.LoginResponse
import com.teamfilmo.filmo.data.remote.model.user.SignUpRequest
import com.teamfilmo.filmo.data.remote.model.user.SignUpResult
import com.teamfilmo.filmo.data.remote.service.AuthService
import com.teamfilmo.filmo.data.source.AuthRemoteDataSource
import javax.inject.Inject

class AuthRemoteDataSourceImpl
    @Inject
    constructor(
        private val authService: AuthService,
    ) : AuthRemoteDataSource {
        override suspend fun signUp(
            request: SignUpRequest,
        ): Result<SignUpResult> = authService.signUp(request)

        override suspend fun login(
            loginRequest: LoginRequest,
        ): Result<LoginResponse> = authService.login(loginRequest)
    }
