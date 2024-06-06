package com.teamfilmo.filmo.domain.repository

import com.teamfilmo.filmo.ui.model.bookmark.BookmarkCount
import com.teamfilmo.filmo.ui.model.bookmark.BookmarkList
import com.teamfilmo.filmo.ui.model.bookmark.BookmarkResponse

interface BookmarkRepository {
    suspend fun registBookmark(reportId: String): Result<BookmarkResponse>

    suspend fun deleteBookmark(bookmarkId: Int): Result<String>

    suspend fun getBookmarkList(): Result<BookmarkList>

    suspend fun getBookmarkCount(reportId: String): Result<BookmarkCount>
}
