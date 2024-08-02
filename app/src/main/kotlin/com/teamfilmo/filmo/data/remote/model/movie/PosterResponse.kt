package com.teamfilmo.filmo.data.remote.model.movie

import kotlinx.serialization.Serializable


@Serializable
data class PosterResponse(
    val configuration: Configuration,
    val images: ImagesX
)

@Serializable
data class Configuration(
    val images: Images
)

@Serializable
data class Images(
    val backdrop_sizes: List<String>,
    val base_url: String,
    val logo_sizes: List<String>,
    val poster_sizes: List<String>,
    val profile_sizes: List<String>,
    val secure_base_url: String,
    val still_sizes: List<String>
)
@Serializable
data class ImagesX(
    val backdrops: List<String>,
    val id: Int,
    val logos: List<String>,
    val posters: List<Poster>
)

@Serializable
data class Poster(
    val aspect_ratio: Double,
    val file_path: String,
    val height: Int,
    val width: Int
)
