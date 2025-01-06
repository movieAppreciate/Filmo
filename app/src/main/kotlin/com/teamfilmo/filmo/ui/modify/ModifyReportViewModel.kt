package com.teamfilmo.filmo.ui.modify

import androidx.lifecycle.viewModelScope
import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import com.teamfilmo.filmo.data.remote.entity.report.get.GetReportResponse
import com.teamfilmo.filmo.data.remote.entity.report.update.UpdateReportRequest
import com.teamfilmo.filmo.domain.report.GetReportUseCase
import com.teamfilmo.filmo.domain.report.UpdateReportUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class ModifyReportViewModel
    @Inject
    constructor(
        private val updateReportUseCase: UpdateReportUseCase,
        private val getReportUseCase: GetReportUseCase,
    ) : BaseViewModel<ModifyReportEffect, ModifyReportEvent>() {
        override fun handleEvent(event: ModifyReportEvent) {
            super.handleEvent(event)
        }

        /*
개별 감상문 정보
*/
        private val _getReportResponse =
            MutableStateFlow(
                GetReportResponse(
                    reportId = "",
                    title = "",
                    content = "",
                    userId = "",
                    movieId = 0,
                    tagString = "",
                    complaintCount = 0,
                    replyCount = 0,
                    likeCount = 0,
                    viewCount = 0,
                    imageUrl = "",
                    createDate = "",
                    lastModifiedDate = "",
                    nickname = "",
                ),
            )
        val getReportResponse: StateFlow<GetReportResponse> = _getReportResponse.asStateFlow()

        fun getReport(reportId: String) {
            viewModelScope.launch {
                getReportUseCase(reportId).collect {
                    if (it != null) {
                        _getReportResponse.value = it
                    }
                }
            }
        }

        fun updateReport(request: UpdateReportRequest) {
            viewModelScope.launch {
                updateReportUseCase(request).collect {
                    Timber.d("updateReportUseCase : $it")
                }
            }
        }
    }
