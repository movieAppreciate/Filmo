package com.teamfilmo.filmo.data.remote.model.movie.detail.response

import kotlinx.serialization.Serializable

@Serializable
data class Genre(
    val id: Int?,
    val name: String?,
)
