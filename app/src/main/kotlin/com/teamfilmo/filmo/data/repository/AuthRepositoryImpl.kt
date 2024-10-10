package com.teamfilmo.filmo.data.repository

import com.teamfilmo.filmo.data.remote.model.user.LoginRequest
import com.teamfilmo.filmo.data.remote.model.user.LoginResponse
import com.teamfilmo.filmo.data.remote.model.user.SignUpRequest
import com.teamfilmo.filmo.data.remote.model.user.SignUpResponse
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
        ): Result<SignUpResponse> {
            return authRemoteDataSource.signUp(request)
        }

        override suspend fun login(
            loginRequest: LoginRequest,
        ): Result<LoginResponse> {
            return authRemoteDataSource.login(loginRequest)
        }
    }
