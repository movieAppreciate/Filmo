package com.teamfilmo.filmo.ui.report

import com.teamfilmo.filmo.base.effect.BaseEffect
import com.teamfilmo.filmo.domain.model.report.all.ReportItem

sealed interface AllMovieReportEffect : BaseEffect {
    data class RegistLike(
        val reportId: String,
        val isLiked: Boolean = true,
        val likeCount: Int,
    ) : AllMovieReportEffect

    data class CancelLike(
        val reportId: String,
        val isLiked: Boolean = false,
        val likeCount: Int,
    ) : AllMovieReportEffect

    data class CountLike(
        val reportId: String,
        val likeCount: Int,
    ) : AllMovieReportEffect

    data class RegistBookmark(
        val reportId: String,
    ) : AllMovieReportEffect

    data class DeleteBookmark(
        val reportId: String,
    ) : AllMovieReportEffect

    data class RefreshReport(
        val reportList: List<ReportItem>,
    ) : AllMovieReportEffect
}
