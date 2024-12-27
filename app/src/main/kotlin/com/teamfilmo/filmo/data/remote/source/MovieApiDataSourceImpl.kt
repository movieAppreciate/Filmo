package com.teamfilmo.filmo.data.remote.source

import com.teamfilmo.filmo.data.remote.model.movie.MovieApiResult
import com.teamfilmo.filmo.data.remote.service.MovieApiService
import com.teamfilmo.filmo.data.source.MovieApiDataSource
import javax.inject.Inject
import retrofit2.Response

class MovieApiDataSourceImpl
    @Inject
    constructor(
        private val movieApiService: MovieApiService,
    ) : MovieApiDataSource {
        override suspend fun getUpcomingMovieList(
            api_key: String,
            page: Int,
        ): Response<MovieApiResult> = movieApiService.getUpcomingMovies(api_key, page)
    }
