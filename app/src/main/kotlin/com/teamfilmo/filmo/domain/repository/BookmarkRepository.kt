package com.teamfilmo.filmo.domain.repository

import com.teamfilmo.filmo.data.remote.model.bookmark.BookmarkCountResponse
import com.teamfilmo.filmo.data.remote.model.bookmark.BookmarkListResponse

interface BookmarkRepository {
    suspend fun registBookmark(reportId: String): Result<com.teamfilmo.filmo.data.remote.model.bookmark.BookmarkResponse>

    suspend fun deleteBookmark(bookmarkId: Long): Result<Unit>

    suspend fun getBookmarkList(): Result<BookmarkListResponse>

    suspend fun getBookmarkCount(reportId: String): Result<BookmarkCountResponse>
}
