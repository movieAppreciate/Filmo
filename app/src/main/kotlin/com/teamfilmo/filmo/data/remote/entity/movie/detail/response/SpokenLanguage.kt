package com.teamfilmo.filmo.data.remote.entity.movie.detail.response

import kotlinx.serialization.Serializable

@Serializable
data class SpokenLanguage(
    val english_name: String?,
    val iso_639_1: String?,
    val name: String?,
)
