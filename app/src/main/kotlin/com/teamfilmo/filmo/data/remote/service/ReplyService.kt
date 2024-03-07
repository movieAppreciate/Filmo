package com.teamfilmo.filmo.data.remote.service

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ReplyService {
    /**
     * 댓글 등록
     */
    @POST("/reply/registReply")
    suspend fun registReply(
        /**
         * 감상문 아이디
         */
        @Query("reportId")
        reportId: String,
        /**
         * 유저 아아디
         */
        @Query("userId")
        userId: String,
        /**
         * 댓글 내용
         */
        @Query("content")
        content: String,
        /**
         * 대댓글일 경우 원 댓글 아이디
         */
        @Query("upReplyId")
        upReplyId: String? = null,
    ): Result<String>

    /**
     * 댓글 수정
     */
    @POST("/reply/modifyReply")
    suspend fun modifyReply(
        /**
         * 댓글 아이디
         */
        @Query("replyId")
        replyId: String,
        /**
         * 댓글 내용
         */
        @Query("content")
        content: String,
    ): Result<String>

    /**
     * 서브댓글 가져오기
     */
    @GET("/reply/getSubReplies/{replyId}")
    suspend fun getSubReplies(
        /**
         * 댓글 아이디
         */
        @Query("replyId")
        replyId: String,
    ): Result<String>

    /**
     * 원댓글 전체 가져오기
     */
    @GET("/reply/getReplies/{reportId}")
    suspend fun getReplies(
        /**
         * 감상문 아이디
         */
        @Query("reportId")
        reportId: String,
    ): Result<String>

    /**
     * 댓글 삭제
     */
    @GET("/reply/deleteReply/{replyId}")
    suspend fun deleteReply(
        /**
         * 댓글 아이디
         */
        @Query("replyId")
        replyId: String,
    ): Result<String>
}
