package com.teamfilmo.filmo.ui.follow.follower

import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FollowerViewModel
    @Inject
    constructor() : BaseViewModel<FollowerEffect, FollowerEvent>()
