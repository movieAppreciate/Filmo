package com.teamfilmo.filmo.data.remote.entity.movie.detail.response

import kotlinx.serialization.Serializable

@Serializable
data class ProductionCountry(
    val iso_3166_1: String,
    val name: String,
)
