package com.teamfilmo.filmo.data.remote.entity.movie.detail

import kotlinx.serialization.Serializable

@Serializable
data class DetailMovieRequest(
    val movieId: String,
)
