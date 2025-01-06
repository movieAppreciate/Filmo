package com.teamfilmo.filmo.data.remote.entity.movie.detail.response

import kotlinx.serialization.Serializable

@Serializable
data class ProductionCompany(
    val id: Int?,
    val logo_path: String? = null,
    val name: String?,
    val origin_country: String?,
)
