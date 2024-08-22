package com.teamfilmo.filmo.domain.movie

import com.teamfilmo.filmo.domain.repository.MovieRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class GetMovieBackDropFromDetailUseCase
    @Inject
    constructor(
        private val movieRepository: MovieRepository,
    ) {
        operator fun invoke(movieId: Int): Flow<String> =
            flow {
                val result =
                    movieRepository.searchDetail(movieId)
                        .onFailure {
                            throw it
                        }
                val imageBaseUrl = "https://image.tmdb.org/t/p/original"
                emit(imageBaseUrl + result.getOrNull()?.backdropPath)
            }
                .catch { Timber.e(it) }
    }
