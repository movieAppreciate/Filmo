package com.teamfilmo.filmo.ui.modify

import com.teamfilmo.filmo.base.event.BaseEvent
import com.teamfilmo.filmo.data.remote.entity.report.update.UpdateReportRequest

sealed class UpdateReportEvent : BaseEvent() {
    data class UpdateReport(
        val request: UpdateReportRequest,
    ) : UpdateReportEvent()
}
