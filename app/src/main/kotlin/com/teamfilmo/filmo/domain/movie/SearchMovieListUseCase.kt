package com.teamfilmo.filmo.domain.movie

import com.teamfilmo.filmo.data.remote.entity.movie.MovieContentResult
import com.teamfilmo.filmo.data.remote.entity.movie.MovieRequest
import com.teamfilmo.filmo.domain.repository.MovieRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import timber.log.Timber

class SearchMovieListUseCase
    @Inject
    constructor(
        private val movieRepository: MovieRepository,
    ) {
        private var list = arrayListOf<MovieContentResult>()

        operator fun invoke(
            query: MovieRequest?,
        ): Flow<List<MovieContentResult>> =
            flow {
                val result =
                    query?.let {
                        movieRepository
                            .searchList(query)
                            .onFailure {
                                when (it) {
                                    is HttpException -> Timber.e("Network error: ${it.message}")
                                    else -> Timber.e("Unknown error: ${it.message}")
                                }
                                emit(emptyList())
                            }.onSuccess {
                                list.clear()
                                it.results.forEach {
                                    if (it.posterPath != null) {
                                        list.add(it)
                                        Timber.d("방출하는 Result : ${it.title}")
                                    }
                                }
                                emit(list)
                            }
                    }
            }
    }
