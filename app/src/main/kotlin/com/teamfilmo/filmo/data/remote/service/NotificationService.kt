package com.teamfilmo.filmo.data.remote.service

import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface NotificationService {
    /**
     * 공지사항 수정
     */
    @PUT("/notification/update")
    suspend fun update(
        /**
         * 공지사항 아이디
         */
        @Query("notificationId")
        notificationId: String,
        /**
         * 공지사항 제목
         */
        @Query("title")
        title: String,
        /**
         * 공지사항 내용
         */
        @Query("content")
        content: String,
        /**
         * 공지사항 타입
         */
        @Query("type")
        type: String,
    ): Result<String>

    /**
     * 공지사항 등록
     */
    @PUT("/notification/regist")
    suspend fun regist(
        /**
         * 공지사항 제목
         */
        @Query("title")
        title: String,
        /**
         * 공지사항 내용
         */
        @Query("content")
        content: String,
        /**
         * 공지사항 타입
         */
        @Query("type")
        type: String,
    ): Result<String>

    /**
     * 공지사항 조회
     */
    @GET("/notification/getNoti/{notificationId}")
    suspend fun getNoti(
        /**
         * 공지사항 아이디
         */
        @Path("notificationId")
        notificationId: String,
    ): Result<String>

    /**
     * 공지사항 리스트
     */
    @GET("/notification/getList")
    suspend fun getList(
        /**
         * 마지막에 조회된 공지사항 아이디, 빈 값일 경우 전체 조회
         */
        @Query("notificationId")
        notificationId: String? = null,
    ): Result<String>

    /**
     * 공지사항 삭제
     */
    @DELETE("/notification/delete/{notificationId}")
    suspend fun delete(
        /**
         * 공지사항 아이디
         */
        @Path("notificationId")
        notificationId: String,
    ): Result<String>
}
