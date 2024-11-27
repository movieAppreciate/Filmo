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
                // 1. 레포지토리 호출 시 예외 발생
                val result = reportRepository.searchReport(SearchReportRequest(lastReportId = lastReportId))
                // 2. 예외가 발생하면 onFailure 블럭에서 throw
                result.onFailure {
                    // 예외를 던진다.
                    throw it
                }
                // 3. 성공 시 결과를 emit 한다.
                emit(result.getOrNull())
            }.catch {
                // 4. throw 된 예외를 여기서 잡는다.
                Timber.e(it.localizedMessage)
            }
    }
