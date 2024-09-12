package com.teamfilmo.filmo.ui.follow.following

import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FollowingViewModel
    @Inject
    constructor() : BaseViewModel<FollowingEffect, FollowingEvent>()
