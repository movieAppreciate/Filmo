package com.teamfilmo.filmo.domain.repository

import com.teamfilmo.filmo.data.remote.model.user.LoginRequest
import com.teamfilmo.filmo.data.remote.model.user.LoginResponse
import com.teamfilmo.filmo.data.remote.model.user.SignUpRequest
import com.teamfilmo.filmo.data.remote.model.user.SignUpResult

interface AuthRepository {
    suspend fun signUp(
        request: SignUpRequest,
    ): Result<SignUpResult>

    suspend fun login(
        loginRequest: LoginRequest,
    ): Result<LoginResponse>
}
