package com.teamfilmo.filmo.data.remote.model.report.search

import kotlinx.serialization.Serializable

@Serializable
data class SearchReportRequest(
    val lastReportId: String = "",
    val keyWord: String = "",
    val targetId: String = "",
)
