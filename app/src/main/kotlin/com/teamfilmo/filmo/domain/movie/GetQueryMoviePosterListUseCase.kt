package com.teamfilmo.filmo.domain.movie

import com.teamfilmo.filmo.data.remote.model.movie.MovieRequest
import com.teamfilmo.filmo.domain.repository.MovieRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class GetQueryMoviePosterListUseCase
    @Inject
    constructor(
        private val movieRepository: MovieRepository,
    ) {
        private var list = arrayListOf<String>()

        operator fun invoke(
            query: MovieRequest?,
        ): Flow<List<String>> =
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
                        list.add(it.posterPath)
                    }
                }
                Timber.d("use case에서 emit하는 list 크기 : ${list.size}")
                emit(list)
            }.catch {
                Timber.e(it.localizedMessage)
            }
    }
