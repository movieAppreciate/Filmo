package com.teamfilmo.filmo.domain.repository

import com.teamfilmo.filmo.data.remote.model.movie.MovieRequest
import com.teamfilmo.filmo.data.remote.model.movie.PosterResponse
import com.teamfilmo.filmo.data.remote.model.movie.ThumbnailRequest
import com.teamfilmo.filmo.model.movie.DetailMovieResponse
import com.teamfilmo.filmo.model.movie.MovieResponse

interface MovieRepository {
    suspend fun searchList(
        query: MovieRequest?,
//        page: Int,
    ): Result<MovieResponse>

    suspend fun searchDetail(movieId: Int): Result<DetailMovieResponse>

    suspend fun getVideo(movieId: Int): Result<String>

    suspend fun getPoster(movieId: ThumbnailRequest): Result<PosterResponse>
}
