package com.teamfilmo.filmo.domain.report

import com.teamfilmo.filmo.domain.repository.ReportRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.flow

class GetReportListUseCase
    @Inject
    constructor(
        private val reportRepository: ReportRepository,
    ) {
        operator fun invoke() =
            flow {
                val result = reportRepository.searchReport()
                emit(result.getOrNull()?.reportList ?: emptyList())
            }
    }
