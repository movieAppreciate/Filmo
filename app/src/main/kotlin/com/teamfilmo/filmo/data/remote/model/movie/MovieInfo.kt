package com.teamfilmo.filmo.data.remote.model.movie

data class MovieInfo(
    val movieImage: Int,
    val movieName: String,
    val movieAge: Int,
    val genres: List<Int>,
)
