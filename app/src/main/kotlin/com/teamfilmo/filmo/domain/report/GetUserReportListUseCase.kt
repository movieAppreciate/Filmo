package com.teamfilmo.filmo.domain.report

import com.teamfilmo.filmo.data.remote.entity.report.search.SearchReportItem
import com.teamfilmo.filmo.data.remote.entity.report.search.SearchReportRequest
import com.teamfilmo.filmo.domain.repository.ReportRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import timber.log.Timber

/*
사용자의 감상문 리스트 조회
 */
class GetUserReportListUseCase
    @Inject
    constructor(
        private val reportRepository: ReportRepository,
    ) {
        operator fun invoke(targetId: String): Flow<List<SearchReportItem>> =
            flow {
                val result = reportRepository.searchReport(SearchReportRequest(targetId = targetId))
                result.onFailure {
                    when (it) {
                        is HttpException -> Timber.e("Network error: ${it.message}")
                        else -> Timber.e("Unknown error: ${it.message}")
                    }
                    emit(emptyList())
                }
                result.onSuccess {
                    emit(it.searchReport)
                }
            }
    }
