package com.teamfilmo.filmo.ui.report

import com.teamfilmo.filmo.base.event.BaseEvent

sealed class AllMovieReportEvent : BaseEvent() {
    data object GetMovieInfoList : AllMovieReportEvent()

    data object LoadReport : AllMovieReportEvent()

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
