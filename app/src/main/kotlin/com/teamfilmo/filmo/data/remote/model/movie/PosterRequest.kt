package com.teamfilmo.filmo.data.remote.model.movie

import kotlinx.serialization.Serializable

@Serializable
data class PosterRequest(
    val baseUrl: String,
    val size: String,
    val filePath: String,
)
