package com.teamfilmo.filmo.data.remote.entity.movie.detail.response

import kotlinx.serialization.Serializable

@Serializable
data class Providers(
    val buy: List<Buy>? = null,
    val link: String,
    val rent: List<Rent>? = null,
    val flatrate: List<Flatrate>? = null,
)
