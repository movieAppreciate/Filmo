package com.teamfilmo.filmo.data.remote.source

import com.teamfilmo.filmo.data.remote.entity.user.ExistingUserResponse
import com.teamfilmo.filmo.data.remote.entity.user.login.LoginRequest
import com.teamfilmo.filmo.data.remote.entity.user.login.LoginResponse
import com.teamfilmo.filmo.data.remote.entity.user.signup.SignUpRequest
import com.teamfilmo.filmo.data.remote.entity.user.signup.SignUpResponse
import com.teamfilmo.filmo.data.remote.entity.user.signup.SignUpResult
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
                    // 회원 가입 성공한 경우 : 200
                    200 -> {
                        val successResponse =
                            Json.decodeFromString<SignUpResponse>(
                                response.body().toString(),
                            )
                        Result.success(SignUpResult.Success(successResponse))
                    }

                    // 이미 등록된 계정인 경우 -  202
                    else -> {
                        val existingResponse =
                            Json.decodeFromString<ExistingUserResponse>(
                                response.body().toString(),
                            )
                        Result.success(SignUpResult.Existing(existingResponse))
                    }
                }
            } catch (e: Exception) {
                Result.failure(e)
            }

        override suspend fun login(
            loginRequest: LoginRequest,
        ): Result<LoginResponse> = authService.login(loginRequest)
    }
