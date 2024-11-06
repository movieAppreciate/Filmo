package com.teamfilmo.filmo.data.remote.service

import com.teamfilmo.filmo.data.remote.model.complaint.SaveComplaintRequest
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ComplaintService {
    /**
     * 감상문 신고
     */
    @POST("/complaint/save")
    suspend fun saveComplaint(
        @Body saveComplaintRequest: SaveComplaintRequest,
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
