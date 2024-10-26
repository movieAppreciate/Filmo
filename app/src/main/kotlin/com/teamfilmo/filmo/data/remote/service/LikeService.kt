package com.teamfilmo.filmo.data.remote.service

import com.teamfilmo.filmo.data.remote.model.like.SaveLikeRequest
import com.teamfilmo.filmo.data.remote.model.like.SaveLikeResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LikeService {
    /**
     * 좋아요 등록/취소
     */
    @POST("/like/save")
    suspend fun saveLike(
        /**
         * 좋아요할 타겟 아이디와 타입
         */
        @Body saveLikeRequest: SaveLikeRequest,
    ): Result<SaveLikeResponse>

    @DELETE("/like/cancel")
    suspend fun cancel(
        @Query("likeId")
        likeId: String,
    ): Result<String>

    /**
     * 좋아요 수 확인
     */
    @GET("/like/count")
    suspend fun count(
        /**
         * 감상문 아이디
         */
        @Query("targetId")
        targetId: String,
    ): Result<Int>

    /**
     * 좋아요 확인
     */
    @GET("/like/check")
    suspend fun checkLike(
        @Query("targetId")
        targetId: String,
        @Query("type")
        type: String,
    ): Result<Boolean>
}
