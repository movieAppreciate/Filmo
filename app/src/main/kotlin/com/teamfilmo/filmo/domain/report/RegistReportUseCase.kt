package com.teamfilmo.filmo.domain.report

import com.teamfilmo.filmo.data.remote.entity.report.regist.RegistReportRequest
import com.teamfilmo.filmo.domain.repository.ReportRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import timber.log.Timber

class RegistReportUseCase
    @Inject
    constructor(
        private val reportRepository: ReportRepository,
    ) {
        operator fun invoke(
            request: RegistReportRequest,
        ) =
            flow {
                val result = reportRepository.registReport(request)
                result.onFailure {
                    when (it) {
                        is HttpException -> Timber.e("Network error: ${it.message}")
                        else -> Timber.e("Unknown error: ${it.message}")
                    }
                    emit(null)
                }
                result.onSuccess {
                    emit(it.value)
                }
            }
    }
