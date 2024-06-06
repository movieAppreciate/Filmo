package com.teamfilmo.filmo.ui.model.report

import kotlinx.serialization.Serializable

/*
개별 감상문 조회 시 돌아오는 응답 형태
 */
@Serializable
data class Report(
    val reportId: String,
    val title: String,
    val content: String,
    val userId: String,
    val movieId: Int,
    val tagString: String,
    val complaintCount: Int,
    val replyCount: Int,
    val likeCount: Int,
    val isLike: Boolean,
    val viewCount: Int,
    val imageUrl: String?,
    val createDate: String,
    val lastModifiedDate: String,
)
