package com.teamfilmo.filmo.ui.mypage

import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel
    @Inject
    constructor() : BaseViewModel<MyPageEffect, MyPageEvent>()
