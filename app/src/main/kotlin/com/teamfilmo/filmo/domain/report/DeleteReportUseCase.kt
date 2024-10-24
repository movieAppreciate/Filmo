package com.teamfilmo.filmo.domain.report

import com.teamfilmo.filmo.domain.repository.ReportRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class DeleteReportUseCase
    @Inject
    constructor
    (private val reportRepository: ReportRepository) {
        operator fun invoke(reportId: String): Flow<String> =
            flow {
                val result = reportRepository.deleteReport(reportId)
                result.onFailure {
                    throw it
                }
                emit(result.getOrNull() ?: "error")
            }.catch { Timber.e("failed to delete report, ${it.localizedMessage}") }
    }
