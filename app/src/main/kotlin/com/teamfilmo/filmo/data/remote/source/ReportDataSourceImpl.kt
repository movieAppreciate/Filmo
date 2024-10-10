package com.teamfilmo.filmo.data.remote.source

import com.teamfilmo.filmo.data.remote.model.report.RegistReportRequest
import com.teamfilmo.filmo.data.remote.model.report.RegistReportResponse
import com.teamfilmo.filmo.data.remote.model.report.SearchAllReportRequest
import com.teamfilmo.filmo.data.remote.model.report.SearchReportResponse
import com.teamfilmo.filmo.data.remote.service.ReportService
import com.teamfilmo.filmo.data.source.ReportDataSource
import com.teamfilmo.filmo.model.report.GetReportResponse
import javax.inject.Inject

class ReportDataSourceImpl
    @Inject
    constructor(
        private val reportService: ReportService,
    ) : ReportDataSource {
        override suspend fun searchAllReport(body: SearchAllReportRequest): Result<SearchReportResponse> {
            return reportService.searchAllReport(body)
        }

        override suspend fun searchReport(
            lastReportId: String?,
            keyword: String?,
            targetId: String?,
        ): Result<SearchReportResponse> {
            return reportService.searchReport()
        }

        override suspend fun getReport(reportId: String): Result<GetReportResponse> {
            return reportService.getReport(reportId)
        }

        override suspend fun registReport(
            request: RegistReportRequest,
        ): Result<RegistReportResponse> {
            return reportService.registReport(request)
        }
    }
