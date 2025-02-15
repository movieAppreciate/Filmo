package com.teamfilmo.filmo.data.remote.service

import com.teamfilmo.filmo.data.remote.entity.user.RefreshResponse
import com.teamfilmo.filmo.data.remote.entity.user.login.LoginRequest
import com.teamfilmo.filmo.data.remote.entity.user.login.LoginResponse
import com.teamfilmo.filmo.data.remote.entity.user.signup.SignUpRequest
import kotlinx.serialization.json.JsonElement
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {
    /**
     * 회원가입
     */
    @POST("/user/signup")
    @Headers("NO-AUTH: true")
    suspend fun signUp(
        @Body request: SignUpRequest,
    ): Response<JsonElement>

    /**
     * 로그인
     */
    @POST("/user/login")
    @Headers("NO-AUTH: true")
    suspend fun login(
        /**
         * 소셜로그인으로 부터 받은 uid
         */
        @Body loginRequest: LoginRequest,
    ): Result<LoginResponse>

    /**
     * 토큰 재발급
     */
    @POST("/token/refresh")
    suspend fun refreshToken(
        /**
         * 액세스 토큰
         */
        @Query("accessToken")
        accessToken: String,
        /**
         * 리프레시 토큰
         */
        @Query("refreshToken")
        refreshToken: String,
    ): Result<RefreshResponse>
}
