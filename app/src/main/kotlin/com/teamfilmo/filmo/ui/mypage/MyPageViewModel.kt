package com.teamfilmo.filmo.ui.mypage

import androidx.lifecycle.viewModelScope
import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import com.teamfilmo.filmo.data.remote.model.follow.count.FollowCountResponse
import com.teamfilmo.filmo.data.remote.model.user.UserInfo
import com.teamfilmo.filmo.domain.follow.CountFollowUseCase
import com.teamfilmo.filmo.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MyPageViewModel
    @Inject
    constructor(
        private val userPreferencesRepository: UserPreferencesRepository,
        private val countFollowUseCase: CountFollowUseCase,
    ) : BaseViewModel<MyPageEffect, MyPageEvent>() {
        // 회원가입 시 저장된 내 정보
        private val _userInfo =
            MutableStateFlow(
                UserInfo(
                    userId = "",
                    nickName = "",
                    roles = "",
                ),
            )
        val userInfo = _userInfo.asStateFlow()

        private val _followInfo = MutableStateFlow(FollowCountResponse())
        val followInfo: StateFlow<FollowCountResponse> = _followInfo

        init {
            viewModelScope.launch {
                userPreferencesRepository.getUserInfo().collect {
                    if (it != null) _userInfo.value = it
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
