package com.teamfilmo.filmo.data.remote.entity.movie

import kotlinx.serialization.Serializable

@Serializable
data class Dates(
    val maximum: String,
    val minimum: String,
)
