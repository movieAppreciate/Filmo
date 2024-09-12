package com.teamfilmo.filmo.ui.report.all

import com.teamfilmo.filmo.base.event.BaseEvent

sealed class AllMovieReportEvent : BaseEvent() {
    data class ClickMovie(
        val movieId: Int,
    ) : AllMovieReportEvent()

    data class ClickLike(
        val reportId: String,
    ) : AllMovieReportEvent()

    data class ClickBookmark(
        val reportId: String,
    ) : AllMovieReportEvent()

    data object RefreshReport : AllMovieReportEvent()
}
