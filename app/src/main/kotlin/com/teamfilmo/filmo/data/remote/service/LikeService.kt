package com.teamfilmo.filmo.data.remote.service

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LikeService {
    /**
     * 좋아요 등록/취소
     */
    @POST("/like/regist")
    suspend fun regist(
        /**
         * 감상문 아이디
         */
        @Query("reportId")
        reportId: String,
    ): Result<String>

    @POST("/like/cancel")
    suspend fun cancel(
        @Query("reportId")
        reportId: String,
    ): Result<String>

    /**
     * 좋아요 수 확인
     */
    @GET("/like/count")
    suspend fun count(
        /**
         * 감상문 아이디
         */
        @Query("reportId")
        reportId: String,
    ): Result<Int>

    /**
     * 좋아요 확인
     */
    @GET("/like/check")
    suspend fun check(
        /**
         * 감상문 아이디
         */
        @Query("reportId")
        reportId: String,
    ): Result<Boolean>
}
