package com.teamfilmo.filmo.data.remote.entity.report.update

import kotlinx.serialization.Serializable

@Serializable
data class UpdateReportRequest(
    val reportId: String,
    val title: String,
    val content: String,
    val movieId: String,
    val imageUrl: String,
    val tagString: String,
)
