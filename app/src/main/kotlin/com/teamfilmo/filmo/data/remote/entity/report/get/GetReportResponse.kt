package com.teamfilmo.filmo.data.remote.entity.report.get

import kotlinx.serialization.Serializable

/*
개별 감상문 조회 시 돌아오는 응답 형태
 */
@Serializable
data class GetReportResponse(
    val reportId: String? = null,
    val title: String? = null,
    val content: String? = null,
    val userId: String? = null,
    val nickname: String? = null,
    val movieId: Int? = null,
    val tagString: String? = null,
    val complaintCount: Int? = null,
    val replyCount: Int = 0,
    val likeCount: Int = 0,
    val viewCount: Int = 0,
    val imageUrl: String? = null,
    val createDate: String? = null,
    val lastModifiedDate: String? = null,
)
