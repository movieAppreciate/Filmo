package com.teamfilmo.filmo.ui.report.all

import com.teamfilmo.filmo.base.event.BaseEvent

sealed class AllMovieReportEvent : BaseEvent() {
    data class RegistLike(
        val reportId: String,
    ) : AllMovieReportEvent()

    data class CancelLike(
        val reportId: String,
    ) : AllMovieReportEvent()
}
