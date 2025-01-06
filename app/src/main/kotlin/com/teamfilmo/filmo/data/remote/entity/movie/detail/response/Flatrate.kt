package com.teamfilmo.filmo.data.remote.entity.movie.detail.response

import kotlinx.serialization.Serializable

@Serializable
data class Flatrate(
    val logo_path: String,
    val provider_id: Int,
    val provider_name: String,
    val display_priority: Int,
)
