package com.teamfilmo.filmo.data.source

import com.teamfilmo.filmo.data.remote.model.report.RegistReportRequest
import com.teamfilmo.filmo.data.remote.model.report.RegistReportResponse
import com.teamfilmo.filmo.data.remote.model.report.ReportInfo
import com.teamfilmo.filmo.model.report.Report

interface ReportDataSource {
    suspend fun searchAllReport(): Result<ReportInfo>

    suspend fun searchReport(
        lastReportId: String? = null,
        keyword: String? = null,
        targetId: String? = null,
    ): Result<ReportInfo>

    suspend fun getReport(reportId: String): Result<Report>

    suspend fun registReport(
        loginId: String,
        request: RegistReportRequest,
    ): Result<RegistReportResponse>
}
