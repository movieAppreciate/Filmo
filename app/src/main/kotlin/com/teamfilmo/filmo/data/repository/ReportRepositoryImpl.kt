package com.teamfilmo.filmo.data.remote.source

import com.teamfilmo.filmo.data.remote.model.report.get.GetReportResponse
import com.teamfilmo.filmo.data.remote.model.report.regist.RegistReportRequest
import com.teamfilmo.filmo.data.remote.model.report.regist.RegistReportResponse
import com.teamfilmo.filmo.data.remote.model.report.search.SearchAllReportRequest
import com.teamfilmo.filmo.data.remote.model.report.search.SearchReportRequest
import com.teamfilmo.filmo.data.remote.model.report.search.SearchReportResponse
import com.teamfilmo.filmo.data.remote.model.report.search.SearchUserReportListRequest
import com.teamfilmo.filmo.data.remote.model.report.update.UpdateReportRequest
import com.teamfilmo.filmo.data.source.ReportDataSource
import com.teamfilmo.filmo.domain.repository.ReportRepository
import javax.inject.Inject

class ReportRepositoryImpl
    @Inject
    constructor(
        private val reportDataSource: ReportDataSource,
    ) : ReportRepository {
        override suspend fun updateReport(request: UpdateReportRequest): Result<String> {
            return reportDataSource.updateReport(request)
        }

        override suspend fun deleteReport(reportId: String): Result<String> {
            return reportDataSource.deleteReport(reportId)
        }

        override suspend fun searchUserReport(targetId: SearchUserReportListRequest): Result<SearchReportResponse> {
            return reportDataSource.searchUserReport(targetId)
        }

        override suspend fun searchAllReport(body: SearchAllReportRequest): Result<SearchReportResponse> {
            return reportDataSource.searchAllReport(body)
        }

        override suspend fun searchReport(
            searchReportRequest: SearchReportRequest,
        ): Result<SearchReportResponse> {
            return reportDataSource.searchReport(searchReportRequest)
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
