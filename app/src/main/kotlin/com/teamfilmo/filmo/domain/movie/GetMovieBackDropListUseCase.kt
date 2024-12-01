package com.teamfilmo.filmo.domain.movie

import com.teamfilmo.filmo.data.remote.model.movie.ThumbnailRequest
import com.teamfilmo.filmo.domain.repository.MovieRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import timber.log.Timber

class GetMovieBackDropListUseCase
    @Inject
    constructor(
        private val movieRepository: MovieRepository,
    ) {
        operator fun invoke(movieId: String): Flow<List<String>> =
            flow {
                val result =
                    movieRepository.getPoster(ThumbnailRequest(movieId))
                result.onFailure {
                    when (it) {
                        is HttpException -> Timber.e("Network error: ${it.message}")
                        else -> Timber.e("Unknown error: ${it.message}")
                    }
                }

                val imageBaseUrl = "https://image.tmdb.org/t/p/original"
                val posters = result.getOrNull()?.images?.backdrops
                Timber.d("영화 포스터 result : $result")
                val list = arrayListOf<String>()
                if (!posters.isNullOrEmpty()) {
                    posters.forEach {
                        list.add(imageBaseUrl + it.file_path)
                    }
                    emit(list)
                    Timber.d("영화 포스터 list : $list")
                } else {
                    Timber.e("${result.getOrNull()?.images} poster list is empty or null")
                }
            }
    }
