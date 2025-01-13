package com.teamfilmo.filmo.ui.report

import com.teamfilmo.filmo.base.event.BaseEvent

sealed class AllMovieReportEvent : BaseEvent() {
    data class UpdateReport(
        val reportId: String,
    ) : AllMovieReportEvent()

    data class ClickLike(
        val reportId: String,
    ) : AllMovieReportEvent()

    data class ClickBookmark(
        val reportId: String,
    ) : AllMovieReportEvent()

    data object RefreshReport : AllMovieReportEvent()
}
