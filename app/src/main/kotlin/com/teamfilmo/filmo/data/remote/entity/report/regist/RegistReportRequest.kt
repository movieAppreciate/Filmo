package com.teamfilmo.filmo.data.remote.entity.report.regist

import kotlinx.serialization.Serializable

@Serializable
data class RegistReportRequest(
    val title: String,
    val content: String,
    val movieId: String,
    val imageUrl: String,
    val tagString: String,
)
