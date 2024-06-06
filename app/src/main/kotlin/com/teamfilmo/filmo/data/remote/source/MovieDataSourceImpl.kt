package com.teamfilmo.filmo.data.remote.source

import com.teamfilmo.filmo.data.remote.service.MovieService
import com.teamfilmo.filmo.data.source.MovieDataSource
import com.teamfilmo.filmo.ui.model.movie.DetailMovieResponse
import com.teamfilmo.filmo.ui.model.movie.MovieResponse
import javax.inject.Inject

class MovieDataSourceImpl
    @Inject
    constructor(
        private val movieService: MovieService,
    ) : MovieDataSource {
        override suspend fun searchList(
            query: String,
            page: Int,
        ): Result<MovieResponse> {
            return movieService.searchList(query, page)
        }

        override suspend fun searchDetail(movieId: Int): Result<DetailMovieResponse> {
            return movieService.searchDetail(movieId)
        }

        override suspend fun getVideo(movieId: Int): Result<String> {
            return movieService.getVideo(movieId)
        }

        override suspend fun getPoster(movieId: Int): Result<String> {
            return movieService.getPoster(movieId)
        }
    }
