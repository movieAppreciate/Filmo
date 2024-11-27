package com.teamfilmo.filmo.domain.report

import com.teamfilmo.filmo.data.remote.model.report.search.SearchReportRequest
import com.teamfilmo.filmo.data.remote.model.report.search.SearchReportResponse
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
                try {
                    val result = reportRepository.searchReport(SearchReportRequest(lastReportId = lastReportId))
                    result
                        .onSuccess { searchResult ->
                            emit(searchResult)
                        }.onFailure { exception ->
                            when (exception) {
                                // todo : 이후 UiState를 통해 HttpError 발생 시 에러 메시지 보이기
                                is HttpException -> Timber.e("Network error: ${exception.message}")
                                else -> Timber.e("Unknown error: ${exception.message}")
                            }
                            // 예외 발생 시  기본값  반환
                            emit(SearchReportResponse())
                        }
                } catch (e: Exception) {
                    // result.onFailure에서 처리되지 않은 모든 예외를 최종적으로 잡아낸다.
                    // 예상치 못한 예외 발생 가능성
                    // 1. JSON 파싱 오류
                    // 2. 라이브러리 내부 버그로 인한 예외
                    Timber.e(e, "Unexpected error in GetReportListUseCase")
                    // 예외 발생 시  기본값  반환
                    emit(SearchReportResponse())
                }
            }
    }

// operator fun invoke(targetId: String): Flow<Result<List<SearchReportItem>>> =
//    reportRepository.searchUserReport(
//        SearchUserReportListRequest(targetId),
//    )
