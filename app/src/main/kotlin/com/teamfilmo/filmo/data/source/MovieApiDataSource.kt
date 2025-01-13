package com.teamfilmo.filmo.data.source

import com.teamfilmo.filmo.data.remote.entity.movie.MovieApiResult
import retrofit2.Response

interface MovieApiDataSource {
    suspend fun getUpcomingMovieList(
        api_key: String,
        page: Int,
    ): Response<MovieApiResult>
}
