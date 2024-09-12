package com.teamfilmo.filmo.data.remote.model.movie.detail

import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailRequest(
    val movieId: String,
)
