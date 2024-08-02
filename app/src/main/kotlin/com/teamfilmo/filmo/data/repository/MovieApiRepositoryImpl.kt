package com.teamfilmo.filmo.data.repository

import com.teamfilmo.filmo.data.remote.model.movie.MovieApiResult
import com.teamfilmo.filmo.data.source.MovieApiDataSource
import com.teamfilmo.filmo.domain.repository.MovieApiRepository
import javax.inject.Inject

class MovieApiRepositoryImpl
    @Inject
    constructor(private val movieApiDataSource: MovieApiDataSource) : MovieApiRepository {
        override suspend fun getUpcomingMovieList(
            apiKey: String,
            page: Int,
        ): Result<MovieApiResult> {
            return movieApiDataSource.getUpcomingMovieList(apiKey, page)
        }
    }
