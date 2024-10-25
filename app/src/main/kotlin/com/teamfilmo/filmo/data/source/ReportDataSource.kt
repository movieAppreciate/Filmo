package com.teamfilmo.filmo.data.source

import com.teamfilmo.filmo.data.remote.model.report.get.GetReportResponse
import com.teamfilmo.filmo.data.remote.model.report.regist.RegistReportRequest
import com.teamfilmo.filmo.data.remote.model.report.regist.RegistReportResponse
import com.teamfilmo.filmo.data.remote.model.report.search.SearchAllReportRequest
import com.teamfilmo.filmo.data.remote.model.report.search.SearchReportRequest
import com.teamfilmo.filmo.data.remote.model.report.search.SearchReportResponse
import com.teamfilmo.filmo.data.remote.model.report.search.SearchUserReportListRequest
import com.teamfilmo.filmo.data.remote.model.report.update.UpdateReportRequest

interface ReportDataSource {
    suspend fun deleteReport(reportId: String): Result<String>

    suspend fun updateReport(request: UpdateReportRequest): Result<String>

    suspend fun searchUserReport(searchUserRequest: SearchUserReportListRequest): Result<SearchReportResponse>

    suspend fun searchAllReport(body: SearchAllReportRequest): Result<SearchReportResponse>

    suspend fun searchReport(
        searchReportRequest: SearchReportRequest,
    ): Result<SearchReportResponse>

    suspend fun getReport(reportId: String): Result<GetReportResponse>

    suspend fun registReport(
        request: RegistReportRequest,
    ): Result<RegistReportResponse>
}
