package com.teamfilmo.filmo.ui.report.follow

import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FollowingReportViewModel
    @Inject
    constructor() : BaseViewModel<FollowingReportEffect, FollowingReportEvent>()
