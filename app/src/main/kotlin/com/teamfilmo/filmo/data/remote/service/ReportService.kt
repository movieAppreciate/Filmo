package com.teamfilmo.filmo.data.remote.service

import com.teamfilmo.filmo.model.report.Report
import com.teamfilmo.filmo.model.report.ReportInfo
import retrofit2.http.GET
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
    /**
     * 감상문 등록
     */
    @POST("/report/registReport")
    suspend fun registReport(
        /**
         * 유저 아이디
         */
        @Query("userId")
        userId: String,
        /**
         * 감상문 제목
         */
        @Query("title")
        title: String,
        /**
         * 감상문 내용
         */
        @Query("content")
        content: String,
        /**
         * 선택한 영화 아이디 (TMDB)
         */
        @Query("movieId")
        movieId: String,
        /**
         * 해쉬태그 Example: #해시태그1 #해시태그2
         */
        @Query("tagString")
        tagString: String,
    ): Result<String>

    /**
     * 감상문 수정
     */
    @POST("/report/modifyReport")
    suspend fun modifyReport(
        /**
         * 감상문 아이디
         */
        @Query("reportId")
        reportId: String,
        /**
         * 감상문 제목
         */
        @Query("title")
        title: String,
        /**
         * 감상문 내용
         */
        @Query("content")
        content: String,
        /**
         * 선택한 영화 아이디 (TMDB)
         */
        @Query("movieId")
        movieId: String,
        /**
         * 해쉬태그 Example: #해시태그1 #해시태그2
         */
        @Query("tagString")
        tagString: String,
    ): Result<String>

    /**
     * 감상문 검색
     */
    @GET("/report/searchReport")
    suspend fun searchReport(
        /**
         * 마지막으로 조회된 감상문 아이디
         */
        @Query("lastReportId")
        lastReportId: String? = null,
        /**
         * 검색어
         */
        @Query("keyword")
        keyword: String? = null,
    ): Result<ReportInfo>

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
    ): Result<Report>

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
