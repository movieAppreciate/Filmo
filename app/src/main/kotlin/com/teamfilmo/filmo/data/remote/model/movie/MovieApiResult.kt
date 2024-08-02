package com.teamfilmo.filmo.data.remote.model.movie

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieApiResult(
    val dates: Dates,
    val page: Int,
    val results: List<MovieResult>,
    @SerialName("total_pages") val totalPages: Int,
    @SerialName("total_results") val totalResults: Int,
)
