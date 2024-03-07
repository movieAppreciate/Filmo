package com.teamfilmo.filmo.data.remote.service

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface InquiryService {
    /**
     * 문의사항 등록
     */
    @POST("/inquiry/regist")
    suspend fun registInquiry(
        /**
         * 문의 등록 유저아이디
         */
        @Query("userId") userId: String,
        /**
         * 문의 제목
         */
        @Query("title") title: String,
        /**
         * 문의 내용
         */
        @Query("content") content: String,
        /**
         * 문의 유형
         */
        @Query("category") category: String,
        /**
         * 답변받을 이메일
         */
        @Query("userEmail") userEmail: String,
    ): Result<String>

    @GET("/inquiry/getInquiry/{inquiryId}")
    suspend fun getInquiry(
        @Path("inquiryId") inquiryId: String,
    ): Result<String>
}
