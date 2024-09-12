package com.teamfilmo.filmo.ui.reply

import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReplyViewModel
    @Inject
    constructor() : BaseViewModel<ReplyEffect, ReplyEvent>()
