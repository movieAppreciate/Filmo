package com.teamfilmo.filmo.domain.repository

import com.teamfilmo.filmo.data.remote.model.report.RegistReportRequest
import com.teamfilmo.filmo.data.remote.model.report.RegistReportResponse
import com.teamfilmo.filmo.data.remote.model.report.SearchAllReportRequest
import com.teamfilmo.filmo.data.remote.model.report.SearchReportResponse
import com.teamfilmo.filmo.model.report.GetReportResponse

interface ReportRepository {
    /*
    전체 감상문 리스트 받아오기
     */
    suspend fun searchAllReport(body: SearchAllReportRequest): Result<SearchReportResponse>

    suspend fun searchReport(
        lastReportId: String? = "null",
        keyword: String? = null,
        targetId: String? = null,
    ): Result<SearchReportResponse>

    suspend fun getReport(reportId: String): Result<GetReportResponse>

    suspend fun registReport(
        request: RegistReportRequest,
    ): Result<RegistReportResponse>
}
