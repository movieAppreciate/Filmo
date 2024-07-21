package com.teamfilmo.filmo.domain.bookmark

import com.teamfilmo.filmo.domain.repository.BookmarkRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class DeleteBookmarkUseCase
    @Inject
    constructor(private val bookmarkRepository: BookmarkRepository) {
        operator fun invoke(bookmarkId: Long): Flow<String?> =
            flow {
                val result =
                    bookmarkRepository.deleteBookmark(bookmarkId)
                        .onFailure { throw it }
                emit(result.getOrNull())
            }.catch {
                Timber.e(it)
            }
    }
