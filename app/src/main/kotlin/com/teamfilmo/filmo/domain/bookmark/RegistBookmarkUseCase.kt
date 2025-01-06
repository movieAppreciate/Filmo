package com.teamfilmo.filmo.domain.bookmark

import com.teamfilmo.filmo.data.remote.entity.bookmark.BookmarkResponse
import com.teamfilmo.filmo.data.remote.entity.bookmark.SaveBookmarkRequest
import com.teamfilmo.filmo.domain.repository.BookmarkRepository
import dagger.Reusable
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
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
                        .registBookmark(SaveBookmarkRequest(reportId))
                result.onFailure {
                    when (it) {
                        is HttpException -> Timber.e("Network error: ${it.message}")
                        else -> Timber.e("Unknown error: ${it.message}")
                    }
                    emit(null)
                }
                result.onSuccess {
                    emit(result.getOrNull())
                }
            }
    }
