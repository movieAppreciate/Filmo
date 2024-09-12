package com.teamfilmo.filmo.data.repository

import com.teamfilmo.filmo.data.remote.model.movie.MovieRequest
import com.teamfilmo.filmo.data.remote.model.movie.MovieResponse
import com.teamfilmo.filmo.data.remote.model.movie.PosterResponse
import com.teamfilmo.filmo.data.remote.model.movie.ThumbnailRequest
import com.teamfilmo.filmo.data.remote.model.movie.detail.MovieDetailRequest
import com.teamfilmo.filmo.data.remote.model.movie.detail.response.DetailMovieResponse
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
//           // page: Int,
        ): Result<MovieResponse> {
            return movieDataSource.searchList(query)
        }

        override suspend fun searchDetail(movieId: MovieDetailRequest): Result<DetailMovieResponse> {
            return movieDataSource.searchDetail(movieId)
        }

        override suspend fun getVideo(movieId: Int): Result<String> {
            return movieDataSource.getVideo(movieId)
        }

        override suspend fun getPoster(movieId: ThumbnailRequest): Result<PosterResponse> {
            return movieDataSource.getPoster(movieId)
        }
    }
