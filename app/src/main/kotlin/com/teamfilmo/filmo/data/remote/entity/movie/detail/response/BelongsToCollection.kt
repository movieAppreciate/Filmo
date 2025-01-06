package com.teamfilmo.filmo.data.remote.entity.movie.detail.response

import kotlinx.serialization.Serializable

@Serializable
data class BelongsToCollection(
    val backdrop_path: String?,
    val id: Int?,
    val name: String?,
    val poster_path: String?,
)
