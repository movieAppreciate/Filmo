package com.teamfilmo.filmo.ui.write

import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WriteViewModel
    @Inject
    constructor() : BaseViewModel<WriteEffect, WriteEvent>()
