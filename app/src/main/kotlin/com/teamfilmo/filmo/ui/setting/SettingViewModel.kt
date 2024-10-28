package com.teamfilmo.filmo.ui.setting

import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel
    @Inject
    constructor() : BaseViewModel<SettingEffect, SettingEvent>() {
        override fun handleEvent(event: SettingEvent) {
        }
    }
