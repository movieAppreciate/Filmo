package com.teamfilmo.filmo.domain.report

import com.teamfilmo.filmo.data.remote.model.report.update.UpdateReportRequest
import com.teamfilmo.filmo.domain.repository.ReportRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class UpdateReportUseCase
    @Inject
    constructor(private val reportRepository: ReportRepository) {
        operator fun invoke(request: UpdateReportRequest): Flow<String?> =
            flow {
                val result = reportRepository.updateReport(request)

                result.onFailure {
                    throw it
                }
                // 수정된 감상문의 아아디 리턴
                emit(result.getOrNull())
            }.catch {
                Timber.d("failed to update report : ${it.localizedMessage}")
            }
    }
