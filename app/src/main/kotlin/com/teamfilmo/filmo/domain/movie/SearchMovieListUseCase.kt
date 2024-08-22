package com.teamfilmo.filmo.domain.movie

import com.teamfilmo.filmo.data.remote.model.movie.MovieRequest
import com.teamfilmo.filmo.domain.repository.MovieRepository
import com.teamfilmo.filmo.model.movie.Result
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class SearchMovieListUseCase
    @Inject
    constructor(
        private val movieRepository: MovieRepository,
    ) {
        private var list = arrayListOf<Result>()

        operator fun invoke(
            query: MovieRequest?,
        ): Flow<List<Result>> =
            flow {
                val result =
                    query?.let {
                        movieRepository.searchList(it)
                            .onFailure {
                                throw it
                            }
                    }
                list.clear()
                result?.getOrNull()?.results?.forEach {
                    if (it.posterPath != null) {
                        list.add(it)
                    }
                }
                emit(list)
            }.catch {
                Timber.e(it.localizedMessage)
            }
    }
