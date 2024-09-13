package com.teamfilmo.filmo.ui.write.report

import com.teamfilmo.filmo.base.event.BaseEvent
import com.teamfilmo.filmo.data.remote.model.report.RegistReportRequest

sealed class WriteReportEvent : BaseEvent() {
    data class RegisterReport(
        val request: RegistReportRequest,
    ) : WriteReportEvent()
}
