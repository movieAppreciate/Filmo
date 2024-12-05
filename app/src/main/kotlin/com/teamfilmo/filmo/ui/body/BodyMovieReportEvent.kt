package com.teamfilmo.filmo.ui.body

import com.teamfilmo.filmo.base.event.BaseEvent
import com.teamfilmo.filmo.data.remote.model.report.update.UpdateReportRequest

sealed class BodyMovieReportEvent : BaseEvent() {
    data object SaveBlock : BodyMovieReportEvent()

    data object SaveComplaint : BodyMovieReportEvent()

    data object ClickFollow : BodyMovieReportEvent()

    data class DeleteReport(
        val reportId: String,
    ) : BodyMovieReportEvent()

    data class UpdateReport(
        val updateReportRequest: UpdateReportRequest,
    ) : BodyMovieReportEvent()

    data class ClickLikeButton(
        val reportId: String,
    ) : BodyMovieReportEvent()

    data class ShowReport(
        val reportId: String,
    ) : BodyMovieReportEvent()

    data class ShowMovieInfo(
        val movieId: Int,
    ) : BodyMovieReportEvent()

    data object ClickMoreButton : BodyMovieReportEvent()
}
