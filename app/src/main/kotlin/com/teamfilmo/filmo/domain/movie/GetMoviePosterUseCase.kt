package com.teamfilmo.filmo.domain.movie

import com.teamfilmo.filmo.data.remote.model.movie.ThumbnailRequest
import com.teamfilmo.filmo.domain.repository.MovieRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class GetMoviePosterUseCase
    @Inject
    constructor(
        private val movieRepository: MovieRepository,
    ) {
        operator fun invoke(movieId: ThumbnailRequest): Flow<String> =
            flow {
                val result =
                    movieRepository.getPoster(movieId)
                        .onFailure {
                            throw it
                        }

                val imageBaseUrl = "https://image.tmdb.org/t/p/original"
                val posters = result.getOrNull()?.images?.posters
                Timber.d("영화 포스터 result : $result")
                if (!posters.isNullOrEmpty()) {
                    val filePath = posters.first().file_path
                    Timber.d("최신 영화 포스터 : ${imageBaseUrl + filePath}")
                    emit(imageBaseUrl + filePath)
                } else {
                    Timber.e("${result.getOrNull()?.images} poster list is empty or null")
                }
            }.catch {
                Timber.e(it)
            }
    }
