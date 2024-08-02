package com.teamfilmo.filmo.domain.movie

import com.teamfilmo.filmo.data.remote.model.movie.MovieResult
import com.teamfilmo.filmo.domain.repository.MovieApiRepository
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

class GetUpcomingMovieUseCase
    @Inject
    constructor(
        private val movieApiRepository: MovieApiRepository,
    ) {

        companion object {
            const val SERVICE_KEY = "f05219d9db24ba715593fc3a9c55c641"
        }

        operator fun invoke(): Flow<List<MovieResult>> =
            flow {
                val result =
                    movieApiRepository.getUpcomingMovieList(SERVICE_KEY, 1)
                        .onFailure {
                            throw it
                        }
                emit(result.getOrNull()?.results ?: emptyList())
            }.catch {
                Timber.e(it.localizedMessage)
            }
                .flowOn(Dispatchers.IO)
    }
