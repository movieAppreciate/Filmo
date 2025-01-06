package com.teamfilmo.filmo.domain.movie.detail

import com.teamfilmo.filmo.data.remote.entity.movie.detail.DetailMovieRequest
import com.teamfilmo.filmo.domain.mapper.DetailMovieMapper
import com.teamfilmo.filmo.domain.model.movie.DetailMovie
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
        operator fun invoke(movieId: Int): Flow<DetailMovie?> =
            flow {
                val result =
                    movieRepository.searchDetail(
                        DetailMovieRequest(movieId.toString()),
                    )
                result.onFailure {
                    Timber.e("실패 : ${it.message}")
                }
                emit(DetailMovieMapper.mapperToDetailMovie(result.getOrNull()))
            }
    }
