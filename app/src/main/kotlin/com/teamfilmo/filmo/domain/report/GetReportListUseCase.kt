package com.teamfilmo.filmo.domain.report

import com.teamfilmo.filmo.data.remote.model.report.search.SearchReportRequest
import com.teamfilmo.filmo.domain.repository.ReportRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

/*
    전체 감상문 조회
 */

class GetReportListUseCase
    @Inject
    constructor(
        private val reportRepository: ReportRepository,
    ) {
        operator fun invoke(lastReportId: String?) =
            flow {
                // todo : 그냥 "" 로 넣어줘도 됨 수정할 것
                val result = reportRepository.searchReport(SearchReportRequest(lastReportId = lastReportId))
                result.onFailure {
                    throw it
                }
//                emit(result.getOrNull()?.searchReport ?: emptyList())
                emit(result.getOrNull())
            }.catch {
                Timber.e(it.localizedMessage)
            }
    }
