package com.teamfilmo.filmo.data.remote.source

import com.teamfilmo.filmo.data.remote.model.report.get.GetReportResponse
import com.teamfilmo.filmo.data.remote.model.report.regist.RegistReportRequest
import com.teamfilmo.filmo.data.remote.model.report.regist.RegistReportResponse
import com.teamfilmo.filmo.data.remote.model.report.search.SearchAllReportRequest
import com.teamfilmo.filmo.data.remote.model.report.search.SearchReportResponse
import com.teamfilmo.filmo.data.source.ReportDataSource
import com.teamfilmo.filmo.domain.repository.ReportRepository
import javax.inject.Inject

class ReportRepositoryImpl
    @Inject
    constructor(
        private val reportDataSource: ReportDataSource,
    ) : ReportRepository {
        override suspend fun searchAllReport(body: SearchAllReportRequest): Result<SearchReportResponse> {
            return reportDataSource.searchAllReport(body)
        }

        override suspend fun searchReport(
            lastReportId: String?,
            keyword: String?,
            targetId: String?,
        ): Result<SearchReportResponse> {
            return reportDataSource.searchReport()
        }

        override suspend fun getReport(reportId: String): Result<GetReportResponse> {
            return reportDataSource.getReport(reportId)
        }

        override suspend fun registReport(
            request: RegistReportRequest,
        ): Result<RegistReportResponse> {
            return reportDataSource.registReport(request)
        }
    }
