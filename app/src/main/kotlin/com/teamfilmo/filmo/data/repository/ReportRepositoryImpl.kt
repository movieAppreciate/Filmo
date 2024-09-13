package com.teamfilmo.filmo.data.remote.source

import com.teamfilmo.filmo.data.remote.model.report.RegistReportRequest
import com.teamfilmo.filmo.data.remote.model.report.RegistReportResponse
import com.teamfilmo.filmo.data.remote.model.report.ReportInfo
import com.teamfilmo.filmo.data.remote.model.report.SearchAllReportRequest
import com.teamfilmo.filmo.data.source.ReportDataSource
import com.teamfilmo.filmo.domain.repository.ReportRepository
import com.teamfilmo.filmo.model.report.Report
import javax.inject.Inject

class ReportRepositoryImpl
    @Inject
    constructor(
        private val reportDataSource: ReportDataSource,
    ) : ReportRepository {
        override suspend fun searchAllReport(body: SearchAllReportRequest): Result<ReportInfo> {
            return reportDataSource.searchAllReport(body)
        }

        override suspend fun searchReport(
            lastReportId: String?,
            keyword: String?,
            targetId: String?,
        ): Result<ReportInfo> {
            return reportDataSource.searchReport()
        }

        override suspend fun getReport(reportId: String): Result<Report> {
            return reportDataSource.getReport(reportId)
        }

        override suspend fun registReport(
            request: RegistReportRequest,
        ): Result<RegistReportResponse> {
            return reportDataSource.registReport(request)
        }
    }
