package com.teamfilmo.filmo.data.remote.entity.report.search

import kotlinx.serialization.Serializable

@Serializable
data class SearchReportResponse(
    val searchReportCount: Int = 0,
    val searchReport: List<SearchReportItem> = emptyList(),
    val hasNext: Boolean = false,
)

@Serializable
data class SearchReportItem(
    val reportId: String,
    val title: String,
    val content: String,
    val createDate: String,
    val imageUrl: String? = null,
    val nickname: String?,
    val likeCount: Int,
    val replyCount: Int,
    val bookmarkCount: Int,
)
