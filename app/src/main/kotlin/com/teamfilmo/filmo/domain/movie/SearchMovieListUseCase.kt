package com.teamfilmo.filmo.domain.movie

import com.teamfilmo.filmo.data.remote.model.movie.MovieContentResult
import com.teamfilmo.filmo.data.remote.model.movie.MovieRequest
import com.teamfilmo.filmo.domain.repository.MovieRepository
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
        private var list = arrayListOf<MovieContentResult>()

        operator fun invoke(
            query: MovieRequest?,
        ): Flow<List<MovieContentResult>> =
            flow {
                val result =
                    query?.let {
                        movieRepository.searchList(query)
                            .onFailure {
                                throw it
                            }
                    }
                list.clear()
                result?.getOrNull()?.results?.forEach {
                    if (it.posterPath != null) {
                        list.add(it)
                        Timber.d("방출하는 Result : ${it.title}")
                    }
                }
                emit(list)
            }.catch {
                Timber.e(it.localizedMessage)
            }
    }
