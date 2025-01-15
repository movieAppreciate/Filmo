package com.teamfilmo.filmo.domain.report

import com.teamfilmo.filmo.data.remote.entity.report.update.UpdateReportRequest
import com.teamfilmo.filmo.data.remote.entity.report.update.UpdateReportResponse
import com.teamfilmo.filmo.domain.repository.ReportRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import timber.log.Timber

class UpdateReportUseCase
    @Inject
    constructor(
        private val reportRepository: ReportRepository,
    ) {
        operator fun invoke(request: UpdateReportRequest): Flow<UpdateReportResponse?> =
            flow {
                val result = reportRepository.updateReport(request)
                result.onFailure {
                    when (it) {
                        is HttpException -> Timber.e("Network error: ${it.message}")
                        else -> Timber.e("Unknown error: ${it.message}")
                    }
                    emit(null)
                }
                result.onSuccess {
                    emit(it)
                }
            }
    }
