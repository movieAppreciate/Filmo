package com.teamfilmo.filmo.domain.report

import com.teamfilmo.filmo.data.remote.model.report.RegistReportRequest
import com.teamfilmo.filmo.domain.repository.ReportRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class RegistReportUseCase
    @Inject
    constructor(
        private val reportRepository: ReportRepository,
    ) {
        operator fun invoke(
            loginId: String,
            request: RegistReportRequest,
        ) =
            flow {
                val result = reportRepository.registReport(loginId, request)
                result.onFailure {
                    throw it
                }
                emit(result.getOrNull()?.value)
            }.catch {
                Timber.d("failed to regist report : ${it.cause}")
            }
    }
