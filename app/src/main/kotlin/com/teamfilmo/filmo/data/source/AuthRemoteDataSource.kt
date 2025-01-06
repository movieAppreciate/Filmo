package com.teamfilmo.filmo.data.source

import com.teamfilmo.filmo.data.remote.entity.user.login.LoginRequest
import com.teamfilmo.filmo.data.remote.entity.user.login.LoginResponse
import com.teamfilmo.filmo.data.remote.entity.user.signup.SignUpRequest
import com.teamfilmo.filmo.data.remote.entity.user.signup.SignUpResult

interface AuthRemoteDataSource {
    suspend fun signUp(
        request: SignUpRequest,
    ): Result<SignUpResult>

    suspend fun login(
        loginRequest: LoginRequest,
    ): Result<LoginResponse>
}
