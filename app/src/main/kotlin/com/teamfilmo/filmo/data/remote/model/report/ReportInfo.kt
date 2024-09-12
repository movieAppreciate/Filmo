package com.teamfilmo.filmo.data.remote.model.report

import kotlinx.serialization.Serializable

@Serializable
data class ReportInfo(
    val searchReportCount: Int,
    val searchReport: List<ReportList>,
    val hasNext: Boolean,
)

@Serializable
data class ReportList(
    val reportId: String,
    val title: String,
    val content: String,
    val createDate: String,
    val imageUrl: String?,
    val nickname: String,
    val likeCount: Int,
    val replyCount: Int,
    val bookmarkCount: Int,
)
