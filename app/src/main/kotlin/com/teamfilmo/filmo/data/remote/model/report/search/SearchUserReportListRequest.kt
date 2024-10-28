package com.teamfilmo.filmo.data.remote.model.report.search

import kotlinx.serialization.Serializable

/*
특정 사용자의 감상문 리스트 request
 */
@Serializable
data class SearchUserReportListRequest(
    val targetId: String?,
)
