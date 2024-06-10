package com.teamfilmo.filmo.data.remote.service

import com.teamfilmo.filmo.data.remote.model.follow.FollowResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface FollowService {
    /**
     * 팔로잉 등록
     */
    @POST("follow/regist")
    suspend fun registFollow(
        /**
         * 대상 아이디
         */
        @Query("followTarget") followTarget: String,
    ): Result<FollowResponse>

    /**
     * 팔로잉 취소
     */
    @POST("follow/cancle")
    suspend fun cancelFollow(
        /**
         * 팔로우 id
         */
        @Query("followId") followId: String,
    ): Result<Unit>

    /**
     * 팔로잉 확인
     */
    @GET("follow/isFollow")
    suspend fun isFollow(
        /**
         * 상대 아이디
         */
        @Query("followTarget") followTarget: String,
    ): Result<Boolean>

    /**
     * 팔로잉/차단 목록
     */
    @GET("follow/followingList")
    suspend fun getFollowingList(
        /**
         * 유저 아이디
         */
        @Query("otherUserId") userId: String? = null,
        /**
         * 마지막으로 조회된 유저 아이디,
         */
        @Query("lastUserId") lastUserId: String? = null,
        /**
         * 검색어
         */
        @Query("keyword") keyword: String? = null,
    ): Result<String>

    /**
     * 팔로워 목록
     */
    @GET("follow/followerList")
    suspend fun getFollowerList(
        /**
         * 유저 아이디
         */
        @Query("userId") userId: String,
        /**
         * 마지막으로 조회된 유저 아이디,
         */
        @Query("lastUserId") lastUserId: String? = null,
        /**
         * 검색어
         */
        @Query("keyword") keyword: String? = null,
    ): Result<String>

    /**
     * 팔로잉/팔로워/차단 수 확인
     */
    @GET("follow/countFollow")
    suspend fun countFollow(
        /**
         * 확인할 유저의 아이디
         */
        @Query("userId") userId: String,
    ): Result<String>
}
