package com.teamfilmo.filmo.data.source

import com.teamfilmo.filmo.data.remote.model.movie.MovieApiResult

interface MovieApiDataSource {
    suspend fun getUpcomingMovieList(
        api_key: String,
        page: Int,
    ): Result<MovieApiResult>
}
