package com.teamfilmo.filmo.data.remote.source

import com.teamfilmo.filmo.data.remote.model.report.get.GetReportResponse
import com.teamfilmo.filmo.data.remote.model.report.regist.RegistReportRequest
import com.teamfilmo.filmo.data.remote.model.report.regist.RegistReportResponse
import com.teamfilmo.filmo.data.remote.model.report.search.SearchAllReportRequest
import com.teamfilmo.filmo.data.remote.model.report.search.SearchReportRequest
import com.teamfilmo.filmo.data.remote.model.report.search.SearchReportResponse
import com.teamfilmo.filmo.data.remote.model.report.search.SearchUserReportListRequest
import com.teamfilmo.filmo.data.remote.model.report.update.UpdateReportRequest
import com.teamfilmo.filmo.data.remote.service.ReportService
import com.teamfilmo.filmo.data.source.ReportDataSource
import javax.inject.Inject

class ReportDataSourceImpl
    @Inject
    constructor(
        private val reportService: ReportService,
    ) : ReportDataSource {
        override suspend fun deleteReport(reportId: String): Result<String> =
            reportService.deleteReport(reportId)

        override suspend fun updateReport(request: UpdateReportRequest): Result<String> = reportService.updateReport(request)

        override suspend fun searchUserReport(searchUserRequest: SearchUserReportListRequest): Result<SearchReportResponse> = reportService.searchUserReport(searchUserRequest)

        override suspend fun searchAllReport(body: SearchAllReportRequest): Result<SearchReportResponse> = reportService.searchAllReport(body)

        override suspend fun searchReport(
            searchReportRequest: SearchReportRequest,
        ): Result<SearchReportResponse> = reportService.searchReport(searchReportRequest)

        override suspend fun getReport(reportId: String): Result<GetReportResponse> = reportService.getReport(reportId)

        override suspend fun registReport(
            request: RegistReportRequest,
        ): Result<RegistReportResponse> = reportService.registReport(request)
    }
