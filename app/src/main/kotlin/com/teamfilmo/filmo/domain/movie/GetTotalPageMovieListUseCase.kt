package com.teamfilmo.filmo.domain.movie

import com.teamfilmo.filmo.data.remote.model.movie.MovieRequest
import com.teamfilmo.filmo.domain.repository.MovieRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class GetTotalPageMovieListUseCase
    @Inject
    constructor(
        private val movieRepository: MovieRepository,
    ) {
        operator fun invoke(query: MovieRequest?): Flow<Int> =
            flow {
                val result =
                    query?.let {
                        movieRepository.searchList(it)
                            .onFailure {
                                throw it
                            }
                    }
                emit(result?.getOrNull()?.totalPages ?: 1)
            }.catch {
                Timber.d("failed to get total pages : ${it.cause}")
            }
    }
