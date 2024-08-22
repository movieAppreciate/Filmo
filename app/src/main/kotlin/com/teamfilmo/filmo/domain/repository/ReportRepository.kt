package com.teamfilmo.filmo.domain.repository

import com.teamfilmo.filmo.data.remote.model.report.RegistReportRequest
import com.teamfilmo.filmo.data.remote.model.report.RegistReportResponse
import com.teamfilmo.filmo.data.remote.model.report.ReportInfo
import com.teamfilmo.filmo.model.report.Report

interface ReportRepository {
    suspend fun searchReport(
        lastReportId: String? = null,
        keyword: String? = null,
    ): Result<ReportInfo>

    suspend fun getReport(reportId: String): Result<Report>

    suspend fun registReport(
        loginId: String,
        request: RegistReportRequest,
    ): Result<RegistReportResponse>
}
