package com.teamfilmo.filmo.data.remote.source

import com.teamfilmo.filmo.data.remote.entity.movie.MovieRequest
import com.teamfilmo.filmo.data.remote.entity.movie.MovieResponse
import com.teamfilmo.filmo.data.remote.entity.movie.PosterResponse
import com.teamfilmo.filmo.data.remote.entity.movie.ThumbnailRequest
import com.teamfilmo.filmo.data.remote.entity.movie.detail.DetailMovieRequest
import com.teamfilmo.filmo.data.remote.entity.movie.detail.response.DetailMovieEntity
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
        ): Result<MovieResponse> = movieService.searchList(query)

        override suspend fun searchDetail(movieId: DetailMovieRequest): Result<DetailMovieEntity> = movieService.searchDetail(movieId)

        override suspend fun getVideo(movieId: Int): Result<String> = movieService.getVideo(movieId)

        override suspend fun getPoster(movieId: ThumbnailRequest): Result<PosterResponse> = movieService.getPoster(movieId)
    }
