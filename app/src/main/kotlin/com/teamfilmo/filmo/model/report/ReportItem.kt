package com.teamfilmo.filmo.model.report

import com.teamfilmo.filmo.data.remote.model.bookmark.BookmarkResponse

/*
 전체 감상문 조회 시 돌아오는 응답 형태 + 필요한 데이터 조합
 */
data class ReportItem(
    val reportId: String,
    val title: String,
    val content: String,
    val createDate: String,
    val imageUrl: String?,
    val nickname: String,
    val likeCount: Int,
    val replyCount: Int,
    val bookmarkCount: Int,
    val bookmarkList: List<BookmarkResponse>?,
)
