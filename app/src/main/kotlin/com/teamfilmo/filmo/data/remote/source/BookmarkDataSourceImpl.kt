package com.teamfilmo.filmo.data.remote.source

import com.teamfilmo.filmo.data.remote.model.bookmark.BookmarkCountResponse
import com.teamfilmo.filmo.data.remote.model.bookmark.BookmarkListResponse
import com.teamfilmo.filmo.data.remote.model.bookmark.BookmarkResponse
import com.teamfilmo.filmo.data.remote.service.BookmarkService
import com.teamfilmo.filmo.data.source.BookmarkDataSource
import javax.inject.Inject

class BookmarkDataSourceImpl
    @Inject
    constructor(
        private val bookmarkService: BookmarkService,
    ) : BookmarkDataSource {
        override suspend fun registBookmark(reportId: String): Result<BookmarkResponse> {
            return bookmarkService.registerBookmark(reportId)
        }

        override suspend fun deleteBookmark(bookmarkId: Long): Result<Unit> {
            return bookmarkService.deleteBookmark(bookmarkId)
        }

        override suspend fun getBookmarkList(): Result<BookmarkListResponse> {
            return bookmarkService.getBookmarkList()
        }

        override suspend fun getBookmarkCount(reportId: String): Result<BookmarkCountResponse> {
            return bookmarkService.getBookmarkCount(reportId)
        }
    }
