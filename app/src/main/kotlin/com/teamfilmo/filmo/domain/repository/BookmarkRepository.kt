package com.teamfilmo.filmo.domain.repository

import com.teamfilmo.filmo.data.remote.entity.bookmark.BookmarkCountResponse
import com.teamfilmo.filmo.data.remote.entity.bookmark.BookmarkListResponse
import com.teamfilmo.filmo.data.remote.entity.bookmark.SaveBookmarkRequest

interface BookmarkRepository {
    suspend fun registBookmark(reportId: SaveBookmarkRequest): Result<com.teamfilmo.filmo.data.remote.entity.bookmark.BookmarkResponse>

    suspend fun deleteBookmark(bookmarkId: Long): Result<String>

    suspend fun getBookmarkList(): Result<BookmarkListResponse>

    suspend fun getBookmarkCount(reportId: String): Result<BookmarkCountResponse>
}
