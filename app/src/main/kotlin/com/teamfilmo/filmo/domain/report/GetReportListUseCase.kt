package com.teamfilmo.filmo.domain.report

import com.teamfilmo.filmo.data.remote.entity.report.search.SearchReportRequest
import com.teamfilmo.filmo.data.remote.entity.report.search.SearchReportResponse
import com.teamfilmo.filmo.domain.repository.ReportRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import timber.log.Timber

/*
    전체 감상문 조회
 */

class GetReportListUseCase
    @Inject
    constructor(
        private val reportRepository: ReportRepository,
    ) {
        operator fun invoke(lastReportId: String?): Flow<SearchReportResponse?> =
            flow {
                // 1. 레포지토리 호출 시 예외 발생
                val result = reportRepository.searchReport(SearchReportRequest(lastReportId = lastReportId))
                result
                    .onSuccess {
                        emit(it)
                    }.onFailure { exception ->
                        // 에러 헨들링
                        when (exception) {
                            // todo : 이후 UiState를 통해 HttpError 발생 시 에러 메시지 보이기
                            is HttpException -> Timber.e("Network error: ${exception.message}")
                            else -> Timber.e("Unknown error: ${exception.message}")
                        }
                        // 예외 발생 시  기본값  반환
                        emit(null)
                    }
            }
    }
