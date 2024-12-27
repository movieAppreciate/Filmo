package com.teamfilmo.filmo.domain.movie

import com.teamfilmo.filmo.data.remote.model.movie.MovieResult
import com.teamfilmo.filmo.domain.repository.MovieApiRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import timber.log.Timber

class GetUpcomingMovieUseCase
    @Inject
    constructor(
        private val movieApiRepository: MovieApiRepository,
    ) {
        companion object {
            const val SERVICE_KEY = "a03cd18043a24a01005b3c585cdcc01a"
        }

        operator fun invoke(): Flow<List<MovieResult>> =
            flow {
                val result =
                    movieApiRepository.getUpcomingMovieList(SERVICE_KEY, 1)
                result.onSuccess {
                    if (it != null) emit(it.results)
                }
                result.onFailure {
                    when (it) {
                        is HttpException -> Timber.e("Network error: ${it.message}")
                        else -> Timber.e("Unknown error: ${it.message}")
                    }
                    emit(emptyList())
                }
            }
    }
