package com.teamfilmo.filmo.domain.movie.detail

import com.teamfilmo.filmo.data.remote.model.movie.detail.DetailMovieRequest
import com.teamfilmo.filmo.domain.repository.MovieRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class GetMovieNameUseCase
    @Inject
    constructor(
        private val movieRepository: MovieRepository,
    ) {
        operator fun invoke(movieId: DetailMovieRequest): Flow<String> =
            flow {
                val result = movieRepository.searchDetail(movieId)
                result.onFailure {
                    throw it
                }
                emit(result.getOrNull()?.title.toString())
            }.catch {
                Timber.d("failed to get movie name : ${it.cause}")
            }
    }
