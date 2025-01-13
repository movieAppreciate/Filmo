package com.teamfilmo.filmo.ui.modify

import androidx.lifecycle.SavedStateHandle
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

@HiltViewModel
class UpdateReportViewModel
    @Inject
    constructor(
        private val savedStateHandle: SavedStateHandle,
        private val updateReportUseCase: UpdateReportUseCase,
        private val getReportUseCase: GetReportUseCase,
    ) : BaseViewModel<UpdateReportEffect, UpdateReportEvent>() {
        companion object {
            private const val KEY_REPORT_ID = "reportId" // args와 동일한 키 사용!
        }

        init {
            getReport()
        }

        val reportId: String? = savedStateHandle[KEY_REPORT_ID]

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

        override fun handleEvent(event: UpdateReportEvent) {
            when (event) {
                is UpdateReportEvent.UpdateReport -> {
                    updateReport(event.request)
                }
            }
        }

        private fun getReport() {
            viewModelScope.launch {
                if (reportId == null) return@launch
                getReportUseCase(reportId).collect {
                    if (it != null) {
                        _getReportResponse.value = it
                    }
                }
            }
        }

        private fun updateReport(request: UpdateReportRequest) {
            viewModelScope.launch {
                updateReportUseCase(request).collect {
                    if (it != null) {
                        sendEffect(UpdateReportEffect.UpdateSuccess)
                    }
                }
            }
        }
    }
