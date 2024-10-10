package com.teamfilmo.filmo.domain.movie.detail

import com.teamfilmo.filmo.data.remote.model.movie.detail.DetailMovieRequest
import com.teamfilmo.filmo.data.remote.model.movie.detail.response.DetailMovieResponse
import com.teamfilmo.filmo.domain.repository.MovieRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class SearchMovieDetailUseCase
    @Inject
    constructor(
        private val movieRepository: MovieRepository,
    ) {
        operator fun invoke(movieId: Int): Flow<DetailMovieResponse?> =
            flow {
                val result =
                    movieRepository.searchDetail(
                        DetailMovieRequest(movieId.toString()),
                    )
                result.onFailure {
                    Timber.e("실패 : ${it.message}")
                }
                Timber.d("상세 정보 : ${result.getOrNull()?.id}")
                emit(result.getOrNull())
            }
    }
