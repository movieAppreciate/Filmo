package com.teamfilmo.filmo.data.remote.source

import com.teamfilmo.filmo.data.remote.model.movie.MovieApiResult
import com.teamfilmo.filmo.data.remote.service.MovieApiService
import com.teamfilmo.filmo.data.source.MovieApiDataSource
import javax.inject.Inject

class MovieApiDataSourceImpl
    @Inject
    constructor(
        private val movieApiService: MovieApiService,
    ) : MovieApiDataSource {
        override suspend fun getUpcomingMovieList(
            api_key: String,
            page: Int,
        ): Result<MovieApiResult> {
            return movieApiService.getUpcomingMovies(api_key, page)
        }
    }
