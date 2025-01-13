package com.teamfilmo.filmo.data.remote.service

import com.teamfilmo.filmo.data.remote.entity.movie.MovieApiResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiService {
    /*
    최신 영화 리스트
     */
    @GET("3/movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("api_key") apikey: String = "",
        @Query("page") page: Int,
        @Query("language") language: String = "ko",
    ): Response<MovieApiResult>
}
