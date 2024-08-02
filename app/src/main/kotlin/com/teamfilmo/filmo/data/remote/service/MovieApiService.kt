package com.teamfilmo.filmo.data.remote.service

import com.teamfilmo.filmo.data.remote.model.movie.MovieApiResult
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiService {
    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("api_key") apikey: String = "",
        @Query("page") page: Int,
        @Query("language") language: String = "ko",
    ): Result<MovieApiResult>
}
