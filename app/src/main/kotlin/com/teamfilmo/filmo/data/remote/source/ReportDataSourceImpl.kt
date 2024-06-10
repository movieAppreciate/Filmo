package com.teamfilmo.filmo.data.remote.source

import com.teamfilmo.filmo.data.remote.service.ReportService
import com.teamfilmo.filmo.data.source.ReportDataSource
import com.teamfilmo.filmo.model.report.Report
import com.teamfilmo.filmo.model.report.ReportInfo
import javax.inject.Inject

class ReportDataSourceImpl
    @Inject
    constructor(
        private val reportService: ReportService,
    ) : ReportDataSource {
        override suspend fun searchReport(): Result<ReportInfo> {
            return reportService.searchReport()
        }

        override suspend fun getReport(reportId: String): Result<Report> {
            return reportService.getReport(reportId)
        }
    }
