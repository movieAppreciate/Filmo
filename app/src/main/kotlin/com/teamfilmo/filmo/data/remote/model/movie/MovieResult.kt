package com.teamfilmo.filmo.data.remote.model.movie

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieResult(
    @SerialName("adult") val adult: Boolean = false,
    @SerialName("backdrop_path") val backdropPath: String = "",
    @SerialName("genre_ids") val genres: List<Int>,
    @SerialName("id") val id: Int,
    @SerialName("original_language") val language: String = "",
    @SerialName("original_title") val originalTitle: String = "",
    @SerialName("overview") val overview: String = "",
    @SerialName("popularity") val popularity: Double = 0.0,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("release_date") val releaseDate: String = "",
    @SerialName("title") val title: String = "",
    @SerialName("video") val video: Boolean = false,
    @SerialName("vote_average") val rating: Double = 0.0,
    @SerialName("vote_count") val vCount: Long = 0L,
)
