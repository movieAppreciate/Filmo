package com.teamfilmo.filmo.data.repository

import com.teamfilmo.filmo.data.remote.entity.movie.MovieRequest
import com.teamfilmo.filmo.data.remote.entity.movie.MovieResponse
import com.teamfilmo.filmo.data.remote.entity.movie.PosterResponse
import com.teamfilmo.filmo.data.remote.entity.movie.ThumbnailRequest
import com.teamfilmo.filmo.data.remote.entity.movie.detail.DetailMovieRequest
import com.teamfilmo.filmo.data.remote.entity.movie.detail.response.DetailMovieEntity
import com.teamfilmo.filmo.data.source.MovieDataSource
import com.teamfilmo.filmo.domain.repository.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl
    @Inject
    constructor(
        private val movieDataSource: MovieDataSource,
    ) : MovieRepository {
        override suspend fun searchList(
            query: MovieRequest?,
        ): Result<MovieResponse> = movieDataSource.searchList(query)

        override suspend fun searchDetail(movieId: DetailMovieRequest): Result<DetailMovieEntity> = movieDataSource.searchDetail(movieId)

        override suspend fun getVideo(movieId: Int): Result<String> = movieDataSource.getVideo(movieId)

        override suspend fun getPoster(movieId: ThumbnailRequest): Result<PosterResponse> = movieDataSource.getPoster(movieId)
    }
