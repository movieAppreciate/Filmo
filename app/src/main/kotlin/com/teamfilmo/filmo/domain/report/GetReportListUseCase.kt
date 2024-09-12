package com.teamfilmo.filmo.domain.report

import com.teamfilmo.filmo.domain.repository.ReportRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class GetReportListUseCase
    @Inject
    constructor(
        private val reportRepository: ReportRepository,
    ) {
        operator fun invoke() =
            flow {
                val result = reportRepository.searchAllReport()
                result.onFailure {
                    throw it
                }
                emit(result.getOrNull()?.searchReport ?: emptyList())
            }.catch {
                Timber.e(it.localizedMessage)
            }
    }
