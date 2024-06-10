package com.teamfilmo.filmo.domain.repository

import com.teamfilmo.filmo.model.report.Report
import com.teamfilmo.filmo.model.report.ReportInfo

interface ReportRepository {
    suspend fun searchReport(): Result<ReportInfo>

    suspend fun getReport(reportId: String): Result<Report>
}
