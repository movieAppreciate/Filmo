package com.teamfilmo.filmo.domain.movie

import com.teamfilmo.filmo.domain.repository.MovieRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class GetMovieImageUseCase
    @Inject
    constructor(
        private val movieRepository: MovieRepository,
    ) {
        operator fun invoke(movieId: Int): Flow<String> =
            flow {
                val result =
                    movieRepository.getPoster(movieId)
                        .onFailure {
                            throw it
                        }

                val imageUrl = "https://image.tmdb.org/t/p/original"
                result.getOrNull()?.images?.posters?.first()?.file_path.apply {
                    emit(imageUrl + this)
                    Timber.d("경로 : ${imageUrl + this}")
                }
            }.catch {
                Timber.e(it.localizedMessage)
            }
    }
