package com.teamfilmo.filmo.data.remote.source

import com.teamfilmo.filmo.data.remote.model.movie.MovieRequest
import com.teamfilmo.filmo.data.remote.model.movie.MovieResponse
import com.teamfilmo.filmo.data.remote.model.movie.PosterResponse
import com.teamfilmo.filmo.data.remote.model.movie.ThumbnailRequest
import com.teamfilmo.filmo.data.remote.model.movie.detail.MovieDetailRequest
import com.teamfilmo.filmo.data.remote.model.movie.detail.response.DetailMovieResponse
import com.teamfilmo.filmo.data.remote.service.MovieService
import com.teamfilmo.filmo.data.source.MovieDataSource
import javax.inject.Inject

class MovieDataSourceImpl
    @Inject
    constructor(
        private val movieService: MovieService,
    ) : MovieDataSource {
        override suspend fun searchList(
            query: MovieRequest?,
        ): Result<MovieResponse> {
            return movieService.searchList(query)
        }

        override suspend fun searchDetail(movieId: MovieDetailRequest): Result<DetailMovieResponse> {
            return movieService.searchDetail(movieId)
        }

        override suspend fun getVideo(movieId: Int): Result<String> {
            return movieService.getVideo(movieId)
        }

        override suspend fun getPoster(movieId: ThumbnailRequest): Result<PosterResponse> {
            return movieService.getPoster(movieId)
        }
    }
