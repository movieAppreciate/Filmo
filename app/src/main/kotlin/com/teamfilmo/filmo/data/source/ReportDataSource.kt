package com.teamfilmo.filmo.data.source

import com.teamfilmo.filmo.data.remote.entity.report.delete.DeleteReportResponse
import com.teamfilmo.filmo.data.remote.entity.report.get.GetReportResponse
import com.teamfilmo.filmo.data.remote.entity.report.regist.RegistReportRequest
import com.teamfilmo.filmo.data.remote.entity.report.regist.RegistReportResponse
import com.teamfilmo.filmo.data.remote.entity.report.search.SearchAllReportRequest
import com.teamfilmo.filmo.data.remote.entity.report.search.SearchReportRequest
import com.teamfilmo.filmo.data.remote.entity.report.search.SearchReportResponse
import com.teamfilmo.filmo.data.remote.entity.report.update.UpdateReportRequest
import com.teamfilmo.filmo.data.remote.entity.report.update.UpdateReportResponse

interface ReportDataSource {
    suspend fun deleteReport(reportId: String): Result<DeleteReportResponse>

    suspend fun updateReport(request: UpdateReportRequest): Result<UpdateReportResponse>

    suspend fun searchAllReport(body: SearchAllReportRequest): Result<SearchReportResponse>

    suspend fun searchReport(
        searchReportRequest: SearchReportRequest,
    ): Result<SearchReportResponse>

    suspend fun getReport(reportId: String): Result<GetReportResponse>

    suspend fun registReport(
        request: RegistReportRequest,
    ): Result<RegistReportResponse>
}
