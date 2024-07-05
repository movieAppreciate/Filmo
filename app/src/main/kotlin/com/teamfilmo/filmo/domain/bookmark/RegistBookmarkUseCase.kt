package com.teamfilmo.filmo.domain.bookmark

import com.teamfilmo.filmo.data.remote.model.bookmark.BookmarkResponse
import com.teamfilmo.filmo.domain.repository.BookmarkRepository
import dagger.Reusable
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

@Reusable
class RegistBookmarkUseCase
    @Inject
    constructor(
        private val bookmarkRepository: BookmarkRepository,
    ) {
        operator fun invoke(reportId: String): Flow<BookmarkResponse?> =
            flow {
                val result =
                    bookmarkRepository
                        .registBookmark(reportId)
                        .onFailure {
                            throw it
                        }
                emit(result.getOrNull())
            }.catch {
                Timber.e(it)
            }
    }
