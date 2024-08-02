package com.teamfilmo.filmo.domain.movie

import com.teamfilmo.filmo.domain.repository.MovieRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetMovieImageUseCase @Inject constructor(
    private val movieRepository: MovieRepository,

    )
{
    operator fun invoke(movieId: Int)  : Flow<Int> = flow {
        val result = movieRepository.getVideo(movieId)
            .onFailure {
                throw it
            }
            .onSuccess {


            }

    }
}
