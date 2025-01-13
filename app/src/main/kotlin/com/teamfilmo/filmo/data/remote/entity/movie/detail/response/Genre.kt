package com.teamfilmo.filmo.data.remote.entity.movie.detail.response

import kotlinx.serialization.Serializable

@Serializable
data class Genre(
    val id: Int?,
    val name: String?,
)
