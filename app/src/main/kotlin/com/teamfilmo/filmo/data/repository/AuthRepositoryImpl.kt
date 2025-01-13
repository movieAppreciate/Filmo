package com.teamfilmo.filmo.data.repository

import com.teamfilmo.filmo.data.remote.entity.user.login.LoginRequest
import com.teamfilmo.filmo.data.remote.entity.user.login.LoginResponse
import com.teamfilmo.filmo.data.remote.entity.user.signup.SignUpRequest
import com.teamfilmo.filmo.data.remote.entity.user.signup.SignUpResult
import com.teamfilmo.filmo.data.source.AuthRemoteDataSource
import com.teamfilmo.filmo.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl
    @Inject
    constructor(
        private val authRemoteDataSource: AuthRemoteDataSource,
    ) : AuthRepository {
        override suspend fun signUp(
            request: SignUpRequest,
        ): Result<SignUpResult> = authRemoteDataSource.signUp(request)

        override suspend fun login(
            loginRequest: LoginRequest,
        ): Result<LoginResponse> = authRemoteDataSource.login(loginRequest)
    }
