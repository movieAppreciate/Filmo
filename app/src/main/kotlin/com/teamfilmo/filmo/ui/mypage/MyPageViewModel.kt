package com.teamfilmo.filmo.ui.mypage

import androidx.lifecycle.viewModelScope
import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import com.teamfilmo.filmo.data.remote.model.follow.count.FollowCountResponse
import com.teamfilmo.filmo.domain.follow.CountFollowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MyPageViewModel
    @Inject
    constructor(
        private val countFollowUseCase: CountFollowUseCase,
    ) : BaseViewModel<MyPageEffect, MyPageEvent>() {
        private val _followInfo = MutableStateFlow(FollowCountResponse())
        val followInfo: StateFlow<FollowCountResponse> = _followInfo
        //  todo : 여기서 asStateFlow를 해주는 역할은???

        init {
            viewModelScope.launch {
                countFollowUseCase(null).collect {
                    if (it != null) {
                        _followInfo.value = it
                    }
                }
            }
        }

        override fun handleEvent(event: MyPageEvent) {
        }
    }
