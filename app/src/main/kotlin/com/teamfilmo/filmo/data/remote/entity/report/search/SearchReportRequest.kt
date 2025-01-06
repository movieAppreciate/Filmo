package com.teamfilmo.filmo.data.remote.entity.report.search

import kotlinx.serialization.Serializable

@Serializable
data class SearchReportRequest(
    val lastReportId: String? = null,
    val keyWord: String? = null,
    val targetId: String? = null,
)
