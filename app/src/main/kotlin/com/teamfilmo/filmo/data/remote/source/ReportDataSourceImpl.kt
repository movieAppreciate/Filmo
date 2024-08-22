package com.teamfilmo.filmo.data.remote.source

import com.teamfilmo.filmo.data.remote.model.report.RegistReportRequest
import com.teamfilmo.filmo.data.remote.model.report.RegistReportResponse
import com.teamfilmo.filmo.data.remote.model.report.ReportInfo
import com.teamfilmo.filmo.data.remote.service.ReportService
import com.teamfilmo.filmo.data.source.ReportDataSource
import com.teamfilmo.filmo.model.report.Report
import javax.inject.Inject

class ReportDataSourceImpl
    @Inject
    constructor(
        private val reportService: ReportService,
    ) : ReportDataSource {
        override suspend fun searchReport(
            lastReportId: String?,
            keyword: String?,
        ): Result<ReportInfo> {
            return reportService.searchReport()
        }

        override suspend fun getReport(reportId: String): Result<Report> {
            return reportService.getReport(reportId)
        }

        override suspend fun registReport(
            loginId: String,
            request: RegistReportRequest,
        ): Result<RegistReportResponse> {
            return reportService.registReport(loginId, request)
        }
    }
