package com.teamfilmo.filmo.data.remote.service

import com.teamfilmo.filmo.data.remote.model.report.get.GetReportResponse
import com.teamfilmo.filmo.data.remote.model.report.regist.RegistReportRequest
import com.teamfilmo.filmo.data.remote.model.report.regist.RegistReportResponse
import com.teamfilmo.filmo.data.remote.model.report.search.SearchAllReportRequest
import com.teamfilmo.filmo.data.remote.model.report.search.SearchReportRequest
import com.teamfilmo.filmo.data.remote.model.report.search.SearchReportResponse
import com.teamfilmo.filmo.data.remote.model.report.search.SearchUserReportListRequest
import com.teamfilmo.filmo.data.remote.model.report.update.UpdateReportRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 감상문
 * 감상문 관련 API
 *
 *
 *
 * POST
 * /report/registReport
 * 감상문 등록
 *
 *
 * POST
 * /report/modifyReport
 * 감상문 수정
 *
 *
 * GET
 * /report/searchReport
 * 감상문 검색
 *
 *
 * GET
 * /report/otherReportOfUser
 * 다른 유저의 감상문 검색
 *
 *
 * GET
 * /report/getReport/{reportId}
 * 감상문 조회
 *
 *
 * GET
 * /report/deleteReport/{reportId}
 * 감상문 삭제
 */
interface ReportService {
    /*
    유저 감상문
     */
    @POST("/report/searchReport")
    suspend fun searchUserReport(
        @Body body: SearchUserReportListRequest,
    ): Result<SearchReportResponse>

    /**
     * 감상문 등록
     */
    @POST("/report/save")
    suspend fun registReport(
        /**
         * 해쉬태그 Example: #해시태그1 #해시태그2
         */
        @Body request: RegistReportRequest,
    ): Result<RegistReportResponse>

    /**
     * 감상문 수정
     */
    @PATCH("/report/updateReport")
    suspend fun updateReport(
        @Body updateReportRequest: UpdateReportRequest,
    ): Result<String>

    /*
    전체 감상문 검색
     */
    @POST("/report/searchReport")
    suspend fun searchAllReport(
        @Body body: SearchAllReportRequest,
    ): Result<SearchReportResponse>

    /**
     * 감상문 검색
     */
    @POST("/report/searchReport")
    suspend fun searchReport(
        /**
         * 마지막으로 조회된 감상문 아이디
         */
        @Body requestAllReport: SearchReportRequest? = null,
    ): Result<SearchReportResponse>

    /**
     * 다른 유저의 감상문 검색
     */
    @GET("/report/otherReportOfUser")
    suspend fun otherReportOfUser(
        /**
         * 유저 아이디
         */
        @Query("userId")
        userId: String,
    ): Result<String>

    /**
     * 감상문 조회
     */
    @GET("/report/getReport/{reportId}")
    suspend fun getReport(
        /**
         * 감상문 아이디
         */
        @Path("reportId")
        reportId: String,
    ): Result<GetReportResponse>

    /**
     * 감상문 삭제
     */
    @GET("/report/deleteReport/{reportId}")
    suspend fun deleteReport(
        /**
         * 감상문 아이디
         */
        @Path("reportId")
        reportId: String,
    ): Result<String>
}
