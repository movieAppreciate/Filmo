package com.teamfilmo.filmo.ui.report.all

import com.teamfilmo.filmo.base.effect.BaseEffect

sealed interface AllMovieReportEffect : BaseEffect {
    data class RegistLike(
        val reportId: String,
        val isLiked: Boolean = true,
    ) : AllMovieReportEffect

    data class CancelLike(
        val reportId: String,
        val isLiked: Boolean = false,
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
}