package com.teamfilmo.filmo.ui.body

import com.teamfilmo.filmo.base.event.BaseEvent

sealed class BodyMovieReportEvent : BaseEvent() {
    data class ShowReport(
        val reportId: String,
    ) : BodyMovieReportEvent()

    data class ShowMovieInfo(
        val movieId: Int,
    ) : BodyMovieReportEvent()
}
