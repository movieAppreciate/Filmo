package com.teamfilmo.filmo.data.source

import com.teamfilmo.filmo.data.remote.entity.bookmark.BookmarkCountResponse
import com.teamfilmo.filmo.data.remote.entity.bookmark.BookmarkListResponse
import com.teamfilmo.filmo.data.remote.entity.bookmark.BookmarkResponse
import com.teamfilmo.filmo.data.remote.entity.bookmark.SaveBookmarkRequest

interface BookmarkDataSource {
    suspend fun registBookmark(reportId: SaveBookmarkRequest): Result<BookmarkResponse>

    suspend fun deleteBookmark(bookmarkId: Long): Result<String>

    suspend fun getBookmarkList(): Result<BookmarkListResponse>

    suspend fun getBookmarkCount(reportId: String): Result<BookmarkCountResponse>
}
