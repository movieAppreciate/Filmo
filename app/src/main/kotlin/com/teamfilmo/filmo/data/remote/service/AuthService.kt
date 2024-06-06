package com.teamfilmo.filmo.data.remote.service

import com.teamfilmo.filmo.ui.model.auth.AuthResponse
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {
    /**
     * 회원가입
     */
    @POST("/signup")
    @Headers("NO-AUTH: true")
    suspend fun signUp(
        /**
         * 소셜로그인으로 부터 받은 uid
         */
        @Query("uid")
        uid: String,
        /**
         * 소셜로그인 타입(kakao, google, naver)
         */
        @Query("type")
        type: String,
        /**
         * 프로필 사진 URL
         */
        @Query("profileUrl")
        profileUrl: String? = null,
    ): Result<String>

    /**
     * 로그인
     */
    @POST("/login")
    @Headers("NO-AUTH: true")
    suspend fun login(
        /**
         * 소셜로그인으로 부터 받은 uid
         */
        @Query("uid")
        uid: String,
        /**
         * 소셜로그인 타입(kakao, google, naver)
         */
        @Query("type")
        type: String,
    ): Result<AuthResponse>

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
    ): Result<String>
}
