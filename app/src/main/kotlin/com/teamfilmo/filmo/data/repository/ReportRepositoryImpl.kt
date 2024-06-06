package com.teamfilmo.filmo.data.remote.source

import com.teamfilmo.filmo.data.source.ReportDataSource
import com.teamfilmo.filmo.domain.repository.ReportRepository
import com.teamfilmo.filmo.ui.model.report.Report
import com.teamfilmo.filmo.ui.model.report.ReportInfo
import javax.inject.Inject

class ReportRepositoryImpl
    @Inject
    constructor(
        private val reportDataSource: ReportDataSource,
    ) : ReportRepository {
        override suspend fun searchReport(): Result<ReportInfo> {
            return reportDataSource.searchReport()
        }

        override suspend fun getReport(reportId: String): Result<Report> {
            return reportDataSource.getReport(reportId)
        }
    }
