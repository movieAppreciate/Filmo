package com.teamfilmo.filmo.domain.report

import com.teamfilmo.filmo.domain.repository.ReportRepository
import javax.inject.Inject
import timber.log.Timber

class DeleteReportUseCase
    @Inject
    constructor
    (private val reportRepository: ReportRepository) {
        suspend operator fun invoke(reportId: String): Result<String> {
            val result = reportRepository.deleteReport(reportId)
            return if (result.isSuccess) {
                Result.success("Report deleted")
            } else {
                Timber.e(result.exceptionOrNull())
                Result.failure(result.exceptionOrNull()!!)
            }
        }
    }
