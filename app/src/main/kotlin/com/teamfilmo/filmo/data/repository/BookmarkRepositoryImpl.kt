package com.teamfilmo.filmo.data.repository

import com.teamfilmo.filmo.data.remote.entity.bookmark.BookmarkCountResponse
import com.teamfilmo.filmo.data.remote.entity.bookmark.BookmarkListResponse
import com.teamfilmo.filmo.data.remote.entity.bookmark.BookmarkResponse
import com.teamfilmo.filmo.data.remote.entity.bookmark.SaveBookmarkRequest
import com.teamfilmo.filmo.data.source.BookmarkDataSource
import com.teamfilmo.filmo.domain.repository.BookmarkRepository
import javax.inject.Inject

class BookmarkRepositoryImpl
    @Inject
    constructor(
        private val bookmarkDataSource: BookmarkDataSource,
    ) : BookmarkRepository {
        override suspend fun registBookmark(reportId: SaveBookmarkRequest): Result<BookmarkResponse> {
            return bookmarkDataSource.registBookmark(reportId)
        }

        override suspend fun deleteBookmark(bookmarkId: Long): Result<String> {
            return bookmarkDataSource.deleteBookmark(bookmarkId)
        }

        override suspend fun getBookmarkList(): Result<BookmarkListResponse> {
            return bookmarkDataSource.getBookmarkList()
        }

        override suspend fun getBookmarkCount(reportId: String): Result<BookmarkCountResponse> {
            return bookmarkDataSource.getBookmarkCount(reportId)
        }
    }
