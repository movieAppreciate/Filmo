package com.teamfilmo.filmo.domain.repository

import com.teamfilmo.filmo.data.remote.model.movie.MovieApiResult

interface MovieApiRepository {
    suspend fun getUpcomingMovieList(
        apiKey: String,
        page: Int,
    ): Result<MovieApiResult>
}