package com.teamfilmo.filmo.domain.model.report.all

/*
 전체 감상문 조회 시 돌아오는 응답 형태 + 필요한 데이터 조합
 */
data class ReportItem(
    val reportId: String,
    var title: String,
    var content: String,
    val createDate: String,
    var imageUrl: String?,
    val nickname: String?,
    var likeCount: Int,
    var replyCount: Int,
    val bookmarkCount: Int,
    var isBookmark: Boolean? = null,
    var isLiked: Boolean,
    val movieName: String,
    val likeId: String? = null,
)
