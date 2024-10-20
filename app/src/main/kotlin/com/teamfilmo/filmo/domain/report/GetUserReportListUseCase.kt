package com.teamfilmo.filmo.domain.report

import com.teamfilmo.filmo.data.remote.model.report.search.SearchReportItem
import com.teamfilmo.filmo.data.remote.model.report.search.SearchUserReportListRequest
import com.teamfilmo.filmo.domain.repository.ReportRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
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
                val result = reportRepository.searchUserReport(SearchUserReportListRequest(targetId))
                result.onFailure {
                    throw it
                }
                emit(result.getOrNull()?.searchReport ?: emptyList())
            }.catch {
                Timber.e("failed to search user Report list")
            }
    }
