package com.teamfilmo.filmo.domain.bookmark

import com.teamfilmo.filmo.domain.repository.BookmarkRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import timber.log.Timber

class DeleteBookmarkUseCase
    @Inject
    constructor(
        private val bookmarkRepository: BookmarkRepository,
    ) {
        operator fun invoke(bookmarkId: Long): Flow<String?> =
            flow {
                val result =
                    bookmarkRepository.deleteBookmark(bookmarkId)

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
            }.catch {
                Timber.e(it)
            }
    }
