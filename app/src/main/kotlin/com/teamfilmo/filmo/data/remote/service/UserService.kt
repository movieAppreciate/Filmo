package com.teamfilmo.filmo.data.remote.service

import retrofit2.http.POST
import retrofit2.http.Query

interface UserService {
    /**
     * 유저권한 확인
     */
    @POST("/userRoles")
    suspend fun userRoles(
        /**
         * 조회할 유저의 아아디
         */
        @Query("userId")
        userId: String,
    ): Result<String>

    /**
     * 유저정보 요청
     */
    @POST("/userinfo")
    suspend fun userInfo(
        /**
         * 조회할 유저의 아아디
         */
        @Query("userId")
        userId: String,
    ): Result<String>
}
