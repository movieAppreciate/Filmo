package com.teamfilmo.filmo.ui.write.report

import com.teamfilmo.filmo.base.event.BaseEvent
import com.teamfilmo.filmo.data.remote.model.report.regist.RegistReportRequest

sealed class WriteReportEvent : BaseEvent() {
    data class RegisterReport(
        val request: RegistReportRequest,
    ) : WriteReportEvent()

    data class SaveReportState(
        val reportTitle: String?,
        val reportContent: String?,
        val tagString: String?,
        val posterUri: String?,
    ) : WriteReportEvent()
}
