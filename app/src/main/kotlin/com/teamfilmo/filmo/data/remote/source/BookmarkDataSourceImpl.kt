package com.teamfilmo.filmo.data.remote.source

import com.teamfilmo.filmo.data.remote.entity.bookmark.BookmarkCountResponse
import com.teamfilmo.filmo.data.remote.entity.bookmark.BookmarkListResponse
import com.teamfilmo.filmo.data.remote.entity.bookmark.BookmarkResponse
import com.teamfilmo.filmo.data.remote.entity.bookmark.SaveBookmarkRequest
import com.teamfilmo.filmo.data.remote.service.BookmarkService
import com.teamfilmo.filmo.data.source.BookmarkDataSource
import javax.inject.Inject

class BookmarkDataSourceImpl
    @Inject
    constructor(
        private val bookmarkService: BookmarkService,
    ) : BookmarkDataSource {
        override suspend fun registBookmark(reportId: SaveBookmarkRequest): Result<BookmarkResponse> {
            return bookmarkService.registerBookmark(reportId)
        }

        override suspend fun deleteBookmark(bookmarkId: Long): Result<String> {
            return bookmarkService.deleteBookmark(bookmarkId)
        }

        override suspend fun getBookmarkList(): Result<BookmarkListResponse> {
            return bookmarkService.getBookmarkList()
        }

        override suspend fun getBookmarkCount(reportId: String): Result<BookmarkCountResponse> {
            return bookmarkService.getBookmarkCount(reportId)
        }
    }
