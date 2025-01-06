package com.teamfilmo.filmo.domain.repository

import com.teamfilmo.filmo.data.remote.entity.movie.MovieRequest
import com.teamfilmo.filmo.data.remote.entity.movie.MovieResponse
import com.teamfilmo.filmo.data.remote.entity.movie.PosterResponse
import com.teamfilmo.filmo.data.remote.entity.movie.ThumbnailRequest
import com.teamfilmo.filmo.data.remote.entity.movie.detail.DetailMovieRequest
import com.teamfilmo.filmo.data.remote.entity.movie.detail.response.DetailMovieEntity

interface MovieRepository {
    suspend fun searchList(
        query: MovieRequest?,
    ): Result<MovieResponse>

    suspend fun searchDetail(movieId: DetailMovieRequest): Result<DetailMovieEntity>

    suspend fun getVideo(movieId: Int): Result<String>

    suspend fun getPoster(movieId: ThumbnailRequest): Result<PosterResponse>
}
