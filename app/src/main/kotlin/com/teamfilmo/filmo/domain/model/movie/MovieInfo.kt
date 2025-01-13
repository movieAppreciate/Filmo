package com.teamfilmo.filmo.domain.model.movie

data class MovieInfo(
    val id: Int,
    val movieImage: String,
    val movieName: String,
    val genres: List<Int>,
)
