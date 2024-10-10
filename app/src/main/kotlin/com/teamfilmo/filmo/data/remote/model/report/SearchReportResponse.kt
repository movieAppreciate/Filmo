package com.teamfilmo.filmo.data.remote.model.report

import kotlinx.serialization.Serializable

@Serializable
data class SearchReportResponse(
    val searchReportCount: Int,
    val searchReport: List<SearchReportItem>,
    val hasNext: Boolean,
)

@Serializable
data class SearchReportItem(
    val reportId: String,
    val title: String,
    val content: String,
    val createDate: String,
    val imageUrl: String? = null,
    val nickname: String,
    val likeCount: Int,
    val replyCount: Int,
    val bookmarkCount: Int,
)
