package com.teamfilmo.filmo.data.remote.service

import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ComplaintService {
    /**
     * 감상문 신고
     */
    @POST("/complaint/registComplaint")
    suspend fun registComplaint(
        /**
         * 신고자 id
         */
        @Query("userId") userId: String? = null,
        /**
         * 신고당한 감상문 id
         */
        @Query("reportId") reportId: String? = null,
        /**
         * 신고 내용
         */
        @Query("content") content: String? = null,
    ): Result<String>

    /**
     * 감상문 신고 취소
     */
    @POST("/complaint/deleteComplaint/{complaintId}")
    suspend fun deleteComplaint(
        @Path("complaintId") complaintId: String,
        /**
         * 신고자 아이디
         */
        @Query("userId") userId: String,
        /**
         * 신고당한 감상문 아이디
         */
        @Query("reportId") reportId: String,
    ): Result<String>
}
