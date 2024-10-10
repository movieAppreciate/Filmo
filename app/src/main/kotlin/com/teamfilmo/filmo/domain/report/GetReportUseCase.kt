package com.teamfilmo.filmo.domain.report

import com.teamfilmo.filmo.domain.repository.ReportRepository
import com.teamfilmo.filmo.model.report.GetReportResponse
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class GetReportUseCase
    @Inject
    constructor(
        private val reportRepository: ReportRepository,
    ) {
        operator fun invoke(reportId: String): Flow<GetReportResponse?> =
            flow {
                val result = reportRepository.getReport(reportId)
                result.onFailure {
                    throw it
                }
                emit(result.getOrNull())
            }.catch {
                Timber.d(it.message)
            }
    }
