package com.teamfilmo.filmo.data.remote.entity.movie

import kotlinx.serialization.Serializable

@Serializable
data class PosterResponse(
    val configuration: Configuration,
    val images: ImagesX,
)

@Serializable
data class Configuration(
    val images: Images,
)

@Serializable
data class Images(
    val backdrop_sizes: List<String>,
    val base_url: String,
    val logo_sizes: List<String>,
    val poster_sizes: List<String>,
    val profile_sizes: List<String>,
    val secure_base_url: String,
    val still_sizes: List<String>,
)

@Serializable
data class ImagesX(
    val backdrops: List<BackDrop>? = null,
    val id: Int,
    val logos: List<Logo>? = null,
    val posters: List<Poster>? = null,
)

@Serializable
data class BackDrop(
    val aspect_ratio: Double? = null,
    val height: Int? = null,
    val iso_639_1: String? = null,
    val file_path: String? = null,
    val vote_average: Double? = null,
    val vote_count: Int? = null,
    val width: Int? = null,
)

@Serializable
data class Logo(
    val aspect_ratio: Double,
    val height: Int,
    val width: Int,
    val file_path: String,
)

@Serializable
data class Poster(
    val aspect_ratio: Double,
    val file_path: String,
    val height: Int,
    val width: Int,
)
