package com.teamfilmo.filmo.data.remote.model.report

import kotlinx.serialization.Serializable

@Serializable
data class RegistReportRequest(
    val title: String,
    val content: String,
    val movieId: String,
    val imageUrl: String,
    val tagString: String,
)
