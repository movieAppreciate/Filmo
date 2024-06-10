package com.teamfilmo.filmo.data.remote.service

import com.teamfilmo.filmo.data.remote.model.bookmark.BookmarkCountResponse
import com.teamfilmo.filmo.data.remote.model.bookmark.BookmarkListResponse
import com.teamfilmo.filmo.data.remote.model.bookmark.BookmarkResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface BookmarkService {
    /**
     * 북마크 등록
     */
    @POST("/bookmark/regist")
    suspend fun registerBookmark(
        /**
         * 북마크하려는 감상문 아이디
         */
        @Query("reportId")
        reportId: String,
    ): Result<BookmarkResponse>

    /**
     * 북마크 리스트 조회
     */
    @GET("/bookmark/list")
    suspend fun getBookmarkList(
        /**
         * 마지막으로 조회된 북마크 아이디, 최초는 빈값
         */
        @Query("bookmarkId")
        bookmarkId: String? = null,
    ): Result<BookmarkListResponse>

    /**
     * 북마크 수 조희
     */
    @GET("/bookmark/count")
    suspend fun getBookmarkCount(
        /**
         * 조회하려는 감상문 아이디
         */
        @Query("reportId")
        reportId: String,
    ): Result<BookmarkCountResponse>

    /**
     * 북마크 삭제
     */
    @DELETE("/bookmark/delete")
    suspend fun deleteBookmark(
        /**
         * 북마크 아이디
         */
        @Query("bookmarkId")
        bookmarkId: Long,
    ): Result<Unit>
}
