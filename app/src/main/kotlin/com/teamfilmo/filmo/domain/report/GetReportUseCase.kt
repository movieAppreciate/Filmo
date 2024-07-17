package com.teamfilmo.filmo.domain.report

import com.teamfilmo.filmo.domain.repository.ReportRepository
import javax.inject.Inject

class GetReportUseCase
    @Inject
    constructor(
        private val reportRepository: ReportRepository,
    )
