package com.teamfilmo.filmo.data.remote.model.movie

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieResult(
    val adult: Boolean,
    @SerialName("backdrop_path") val backdropPath: String,
    @SerialName("genre_ids") val genres: List<Int>,
    val id: Int,
    @SerialName("original_language") val language: String,
    @SerialName("original_title") val originalTitle: String,
    val overview: String,
    val popularity: Double,
    @SerialName("poster_path") val posterPath: String,
    @SerialName("release_date") val releaseDate: String,
    val title: String,
    val video: Boolean,
    @SerialName("vote_average") val rating: Double,
    @SerialName("vote_count") val vCount: Long,
)
