package com.teamfilmo.filmo.ui.write.report

import androidx.lifecycle.viewModelScope
import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import com.teamfilmo.filmo.data.remote.model.report.regist.RegistReportRequest
import com.teamfilmo.filmo.domain.report.RegistReportUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class WriteReportViewModel
    @Inject
    constructor(
        private val registReportUseCase: RegistReportUseCase,
    ) : BaseViewModel<WriteReportEffect, WriteReportEvent>() {
        override fun handleEvent(event: WriteReportEvent) {
            when (event) {
                is WriteReportEvent.RegisterReport -> {
                    registerReport(event.request)
                    sendEffect(WriteReportEffect.NavigateToMain)
                }
            }
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
