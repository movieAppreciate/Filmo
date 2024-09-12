package com.teamfilmo.filmo.ui.follow

import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FollowViewModel
    @Inject
    constructor() : BaseViewModel<FollowEffect, FollowEvent>()
