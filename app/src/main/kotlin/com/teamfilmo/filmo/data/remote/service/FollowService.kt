package com.teamfilmo.filmo.data.remote.service

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface FollowService {
    /**
     * 팔로잉/차단 등록
     */
    @POST("follow/regist")
    suspend fun registFollow(
        /**
         * 현재 사용자 아이디
         */
        @Query("userId") userId: String,
        /**
         * 대상 아이디
         */
        @Query("followTarget") followTarget: String,
        /**
         * follow(팔로우) or block(차단)
         */
        @Query("type") type: String,
    ): Result<String>

    /**
     * 팔로잉/차단 취소
     */
    @POST("follow/cancle")
    suspend fun cancleFollow(
        /**
         * 팔로우 id
         */
        @Query("followId") followId: String,
    ): Result<String>

    /**
     * 팔로잉/차단 확인
     */
    @GET("follow/isFollow")
    suspend fun isFollow(
        /**
         * 유저 아이디
         */
        @Query("userId") userId: String,
        /**
         * 상대 아이디
         */
        @Query("followTarget") followTarget: String,
    ): Result<String>

    /**
     * 팔로잉/차단 목록
     */
    @GET("follow/followingList")
    suspend fun getFollowingList(
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
        /**
         * follow(팔로우) or block(차단)
         */
        @Query("type") type: String,
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
