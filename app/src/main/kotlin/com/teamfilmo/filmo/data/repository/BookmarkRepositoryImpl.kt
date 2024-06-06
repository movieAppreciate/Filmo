package com.teamfilmo.filmo.data.repository

import com.teamfilmo.filmo.data.source.BookmarkDataSource
import com.teamfilmo.filmo.domain.repository.BookmarkRepository
import com.teamfilmo.filmo.ui.model.bookmark.BookmarkCount
import com.teamfilmo.filmo.ui.model.bookmark.BookmarkList
import com.teamfilmo.filmo.ui.model.bookmark.BookmarkResponse
import javax.inject.Inject

class BookmarkRepositoryImpl
    @Inject
    constructor(
        private val bookmarkDataSource: BookmarkDataSource,
    ) : BookmarkRepository {
        override suspend fun registBookmark(reportId: String): Result<BookmarkResponse> {
            return bookmarkDataSource.registBookmark(reportId)
        }

        override suspend fun deleteBookmark(bookmarkId: Int): Result<String> {
            return bookmarkDataSource.deleteBookmark(bookmarkId)
        }

        override suspend fun getBookmarkList(): Result<BookmarkList> {
            return bookmarkDataSource.getBookmarkList()
        }

        override suspend fun getBookmarkCount(reportId: String): Result<BookmarkCount> {
            return bookmarkDataSource.getBookmarkCount(reportId)
        }
    }
