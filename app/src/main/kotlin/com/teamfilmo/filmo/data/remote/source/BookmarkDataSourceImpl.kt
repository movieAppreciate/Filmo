package com.teamfilmo.filmo.data.remote.source

import com.teamfilmo.filmo.data.remote.service.BookmarkService
import com.teamfilmo.filmo.data.source.BookmarkDataSource
import com.teamfilmo.filmo.ui.model.bookmark.BookmarkCount
import com.teamfilmo.filmo.ui.model.bookmark.BookmarkList
import com.teamfilmo.filmo.ui.model.bookmark.BookmarkResponse
import javax.inject.Inject

class BookmarkDataSourceImpl
    @Inject
    constructor(
        private val bookmarkService: BookmarkService,
    ) : BookmarkDataSource {
        override suspend fun registBookmark(reportId: String): Result<BookmarkResponse> {
            return bookmarkService.registerBookmark(reportId)
        }

        override suspend fun deleteBookmark(bookmarkId: Int): Result<String> {
            return bookmarkService.deleteBookmark(bookmarkId)
        }

        override suspend fun getBookmarkList(): Result<BookmarkList> {
            return bookmarkService.getBookmarkList()
        }

        override suspend fun getBookmarkCount(reportId: String): Result<BookmarkCount> {
            return bookmarkService.getBookmarkCount(reportId)
        }
    }
