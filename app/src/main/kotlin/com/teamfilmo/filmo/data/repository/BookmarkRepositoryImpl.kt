package com.teamfilmo.filmo.data.repository

import com.teamfilmo.filmo.data.remote.model.bookmark.BookmarkCountResponse
import com.teamfilmo.filmo.data.remote.model.bookmark.BookmarkListResponse
import com.teamfilmo.filmo.data.remote.model.bookmark.BookmarkResponse
import com.teamfilmo.filmo.data.source.BookmarkDataSource
import com.teamfilmo.filmo.domain.repository.BookmarkRepository
import javax.inject.Inject

class BookmarkRepositoryImpl
    @Inject
    constructor(
        private val bookmarkDataSource: BookmarkDataSource,
    ) : BookmarkRepository {
        override suspend fun registBookmark(reportId: String): Result<BookmarkResponse> {
            return bookmarkDataSource.registBookmark(reportId)
        }

        override suspend fun deleteBookmark(bookmarkId: Long): Result<Unit> {
            return bookmarkDataSource.deleteBookmark(bookmarkId)
        }

        override suspend fun getBookmarkList(): Result<BookmarkListResponse> {
            return bookmarkDataSource.getBookmarkList()
        }

        override suspend fun getBookmarkCount(reportId: String): Result<BookmarkCountResponse> {
            return bookmarkDataSource.getBookmarkCount(reportId)
        }
    }
