package com.teamfilmo.filmo.ui.mypage

import androidx.lifecycle.viewModelScope
import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import com.teamfilmo.filmo.data.remote.model.follow.count.FollowCountResponse
import com.teamfilmo.filmo.data.remote.model.user.UserResponse
import com.teamfilmo.filmo.domain.follow.CountFollowUseCase
import com.teamfilmo.filmo.domain.user.GetUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MyPageViewModel
    @Inject
    constructor(
        private val getUserInfoUseCase: GetUserInfoUseCase,
        private val countFollowUseCase: CountFollowUseCase,
    ) : BaseViewModel<MyPageEffect, MyPageEvent>() {
        // 내 정보
        private val _userInfo = MutableStateFlow(UserResponse())
        val userInfo: StateFlow<UserResponse> = _userInfo

        private val _followInfo = MutableStateFlow(FollowCountResponse())
        val followInfo: StateFlow<FollowCountResponse> = _followInfo

        init {
            viewModelScope.launch {
                getUserInfoUseCase(null).collect {
                    if (it != null) {
                        _userInfo.value = it
                    }
                }
            }
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
