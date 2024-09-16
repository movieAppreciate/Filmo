package com.teamfilmo.filmo.data.remote.model.movie.detail.response

import kotlinx.serialization.Serializable

@Serializable
data class Buy(
    val display_priority: Int,
    val logo_path: String,
    val provider_id: Int,
    val provider_name: String,
)
