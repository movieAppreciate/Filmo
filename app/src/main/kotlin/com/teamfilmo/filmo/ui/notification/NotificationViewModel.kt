package com.teamfilmo.filmo.ui.notification

import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel
    @Inject
    constructor() : BaseViewModel<NotificationEffect, NotificationEvent>()
