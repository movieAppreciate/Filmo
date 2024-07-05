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
class GetBookmarkLIstUseCase
    @Inject
    constructor(
        private val bookmarkRepository: BookmarkRepository,
    ) {
        operator fun invoke(): Flow<List<BookmarkResponse>> =
            flow {
                val result = bookmarkRepository.getBookmarkList()
                emit(result.getOrNull()?.bookmarkList ?: emptyList())
            }.catch {
                Timber.e(it)
            }
    }
