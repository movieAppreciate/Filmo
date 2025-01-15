package com.teamfilmo.filmo.data.remote.source

import com.teamfilmo.filmo.data.remote.entity.report.delete.DeleteReportResponse
import com.teamfilmo.filmo.data.remote.entity.report.get.GetReportResponse
import com.teamfilmo.filmo.data.remote.entity.report.regist.RegistReportRequest
import com.teamfilmo.filmo.data.remote.entity.report.regist.RegistReportResponse
import com.teamfilmo.filmo.data.remote.entity.report.search.SearchAllReportRequest
import com.teamfilmo.filmo.data.remote.entity.report.search.SearchReportRequest
import com.teamfilmo.filmo.data.remote.entity.report.search.SearchReportResponse
import com.teamfilmo.filmo.data.remote.entity.report.update.UpdateReportRequest
import com.teamfilmo.filmo.data.remote.entity.report.update.UpdateReportResponse
import com.teamfilmo.filmo.data.source.ReportDataSource
import com.teamfilmo.filmo.domain.repository.ReportRepository
import javax.inject.Inject

class ReportRepositoryImpl
    @Inject
    constructor(
        private val reportDataSource: ReportDataSource,
    ) : ReportRepository {
        override suspend fun updateReport(request: UpdateReportRequest): Result<UpdateReportResponse> = reportDataSource.updateReport(request)

        override suspend fun deleteReport(reportId: String): Result<DeleteReportResponse> = reportDataSource.deleteReport(reportId)

        override suspend fun searchAllReport(body: SearchAllReportRequest): Result<SearchReportResponse> = reportDataSource.searchAllReport(body)

        override suspend fun searchReport(
            searchReportRequest: SearchReportRequest,
        ): Result<SearchReportResponse> = reportDataSource.searchReport(searchReportRequest)

        override suspend fun getReport(reportId: String): Result<GetReportResponse> = reportDataSource.getReport(reportId)

        override suspend fun registReport(
            request: RegistReportRequest,
        ): Result<RegistReportResponse> = reportDataSource.registReport(request)
    }
