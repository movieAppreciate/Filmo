package com.teamfilmo.filmo.ui.report

import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReportViewModel
    @Inject
    constructor() : BaseViewModel<ReportEffect, ReportEvent>()
