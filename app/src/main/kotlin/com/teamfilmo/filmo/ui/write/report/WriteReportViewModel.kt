package com.teamfilmo.filmo.ui.write.report

import androidx.lifecycle.viewModelScope
import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import com.teamfilmo.filmo.data.remote.model.report.regist.RegistReportRequest
import com.teamfilmo.filmo.domain.report.RegistReportUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

data class ReportInfo(
    val title: String? = null,
    val content: String? = null,
    val tagString: String? = null,
    val posterUri: String? = null,
)

@HiltViewModel
class WriteReportViewModel
    @Inject
    constructor(
        private val registReportUseCase: RegistReportUseCase,
    ) : BaseViewModel<WriteReportEffect, WriteReportEvent>() {
        // 작성된 글 정보
        private val _reportInfoStateFlow = MutableStateFlow(ReportInfo())
        val reportInfoStateFlow = _reportInfoStateFlow.asStateFlow()

        override fun handleEvent(event: WriteReportEvent) {
            when (event) {
                is WriteReportEvent.RegisterReport -> {
                    registerReport(event.request)
                    sendEffect(WriteReportEffect.NavigateToMain)
                }
                is WriteReportEvent.SaveReportState -> {
                    saveReport(event.reportTitle, event.reportContent, event.tagString, event.posterUri)
                }
            }
        }

        private fun saveReport(
            title: String?,
            content: String?,
            tagString: String?,
            posterUri: String?,
        ) {
            val report =
                ReportInfo(
                    title,
                    content,
                    tagString,
                    posterUri,
                )
            _reportInfoStateFlow.value = report
        }

        private fun registerReport(
            request: RegistReportRequest,
        ) {
            viewModelScope.launch {
                registReportUseCase(request).collect {
                    Timber.d("regist : $it")
                }
            }
        }
    }
