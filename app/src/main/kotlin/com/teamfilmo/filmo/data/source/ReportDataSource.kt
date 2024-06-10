package com.teamfilmo.filmo.data.source

import com.teamfilmo.filmo.model.report.Report
import com.teamfilmo.filmo.model.report.ReportInfo

interface ReportDataSource {
    suspend fun searchReport(): Result<ReportInfo>

    suspend fun getReport(reportId: String): Result<Report>
}
