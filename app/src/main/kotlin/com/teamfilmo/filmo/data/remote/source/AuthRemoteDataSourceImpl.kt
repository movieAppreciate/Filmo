package com.teamfilmo.filmo.data.remote.source

import com.teamfilmo.filmo.data.remote.model.user.ExistingUserResponse
import com.teamfilmo.filmo.data.remote.model.user.LoginRequest
import com.teamfilmo.filmo.data.remote.model.user.LoginResponse
import com.teamfilmo.filmo.data.remote.model.user.SignUpRequest
import com.teamfilmo.filmo.data.remote.model.user.SignUpResponse
import com.teamfilmo.filmo.data.remote.model.user.SignUpResult
import com.teamfilmo.filmo.data.remote.service.AuthService
import com.teamfilmo.filmo.data.source.AuthRemoteDataSource
import javax.inject.Inject
import kotlinx.serialization.json.Json

class AuthRemoteDataSourceImpl
    @Inject
    constructor(
        private val authService: AuthService,
    ) : AuthRemoteDataSource {
        override suspend fun signUp(
            request: SignUpRequest,
        ): Result<SignUpResult> =
            try {
                val response = authService.signUp(request)
                when (response.code()) {
                    200 -> {
                        val successResponse =
                            Json.decodeFromString<SignUpResponse>(
                                response.body().toString(),
                            )
                        Result.success(SignUpResult.Success(successResponse))
                    }
                    202 -> {
                        val existingResponse =
                            Json.decodeFromString<ExistingUserResponse>(
                                response.body().toString(),
                            )
                        Result.success(SignUpResult.Existing(existingResponse))
                    }
                    else -> Result.failure(Exception("Unknown error"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }

        override suspend fun login(
            loginRequest: LoginRequest,
        ): Result<LoginResponse> = authService.login(loginRequest)
    }
