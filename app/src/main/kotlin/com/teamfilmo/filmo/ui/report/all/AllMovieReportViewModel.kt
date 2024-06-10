package com.teamfilmo.filmo.ui.report.all

import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AllMovieReportViewModel
    @Inject
    constructor() : BaseViewModel<AllMovieReportEffect, AllMovieReportEvent>()
