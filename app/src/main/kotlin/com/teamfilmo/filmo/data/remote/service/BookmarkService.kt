package com.teamfilmo.filmo.data.remote.service

import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface BookmarkService {
    /**
     * 북마크 등록
     */
    @POST("/bookmark/register")
    suspend fun registerBookmark(
        /**
         * 유저 아이디
         */
        @Query("userId")
        userId: String,
        /**
         * 북마크하려는 감상문 아이디
         */
        @Query("reportId")
        reportId: String,
    ): Result<String>

    /**
     * 북마크 리스트 조회
     */
    @GET("/bookmark/list")
    suspend fun getBookmarkList(
        /**
         * 유저 아이디
         */
        @Query("userId")
        userId: String,
        /**
         * 마지막으로 조회된 북마크 아이디, 최초는 빈값
         */
        @Query("bookmarkId")
        bookmarkId: String? = null,
    ): Result<String>

    /**
     * 북마크 삭제
     */
    @DELETE("/bookmark/delete")
    suspend fun deleteBookmark(
        /**
         * 북마크 아이디
         */
        @Query("bookmarkId")
        bookmarkId: String,
    ): Result<String>
}
