package com.teamfilmo.filmo.data.remote.model.movie.detail

import kotlinx.serialization.Serializable

@Serializable
data class DetailMovieRequest(
    val movieId: String,
)
