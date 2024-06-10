package com.teamfilmo.filmo.data.source

import com.teamfilmo.filmo.data.remote.model.bookmark.BookmarkCountResponse
import com.teamfilmo.filmo.data.remote.model.bookmark.BookmarkListResponse
import com.teamfilmo.filmo.data.remote.model.bookmark.BookmarkResponse

interface BookmarkDataSource {
    suspend fun registBookmark(reportId: String): Result<BookmarkResponse>

    suspend fun deleteBookmark(bookmarkId: Long): Result<Unit>

    suspend fun getBookmarkList(): Result<BookmarkListResponse>

    suspend fun getBookmarkCount(reportId: String): Result<BookmarkCountResponse>
}
