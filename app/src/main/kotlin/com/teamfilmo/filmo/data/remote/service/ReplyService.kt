package com.teamfilmo.filmo.data.remote.service

import com.teamfilmo.filmo.data.remote.model.reply.DeleteReplyResponse
import com.teamfilmo.filmo.data.remote.model.reply.get.GetReplyResponseItem
import com.teamfilmo.filmo.data.remote.model.reply.save.SaveReplyRequest
import com.teamfilmo.filmo.data.remote.model.reply.save.SaveReplyResponse
import com.teamfilmo.filmo.data.remote.model.reply.update.UpdateReplyRequest
import com.teamfilmo.filmo.data.remote.model.reply.update.UpdateReplyResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ReplyService {
    /**
     * 댓글 등록
     */
    @POST("/reply/save")
    suspend fun saveReply(
        @Body request: SaveReplyRequest,
    ): Result<SaveReplyResponse>

    /**
     * 댓글 수정
     */
    @POST("/reply/update")
    suspend fun updateReply(
        @Body request: UpdateReplyRequest,
    ): Result<UpdateReplyResponse>

    /**
     * 원댓글 전체 가져오기
     */
    @GET("/reply/get/{reportId}")
    suspend fun getReply(
        /**
         * 감상문 아이디
         */
        @Path("reportId")
        reportId: String,
    ): Result<List<GetReplyResponseItem>>

    /**
     * 댓글 삭제
     */
    @DELETE("/reply/deleteReply/{replyId}")
    suspend fun deleteReply(
        /**
         * 댓글 아이디
         */
        @Path("replyId")
        replyId: String,
    ): Result<DeleteReplyResponse>
}
