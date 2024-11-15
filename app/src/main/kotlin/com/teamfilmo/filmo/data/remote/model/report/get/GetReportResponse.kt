package com.teamfilmo.filmo.data.remote.model.report.get

import kotlinx.serialization.Serializable

/*
개별 감상문 조회 시 돌아오는 응답 형태
 */
@Serializable
data class GetReportResponse(
    val reportId: String,
    val title: String,
    val content: String,
    val userId: String,
    val nickname: String,
    val movieId: Int,
    val tagString: String? = null,
    val complaintCount: Int,
    val replyCount: Int,
    val likeCount: Int,
    val viewCount: Int,
    val imageUrl: String? = null,
    val createDate: String,
    val lastModifiedDate: String,
)
