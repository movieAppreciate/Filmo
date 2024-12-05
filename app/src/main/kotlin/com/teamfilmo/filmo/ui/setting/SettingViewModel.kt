package com.teamfilmo.filmo.ui.setting

import androidx.lifecycle.viewModelScope
import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import com.teamfilmo.filmo.data.remote.model.user.UserInfo
import com.teamfilmo.filmo.domain.repository.UserPreferencesRepository
import com.teamfilmo.filmo.domain.user.DeleteUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SettingViewModel
    @Inject
    constructor(
        private val userPreferencesRepository: UserPreferencesRepository,
        private val deleteUserUseCase: DeleteUserUseCase,
    ) : BaseViewModel<SettingEffect, SettingEvent>() {
        // 내 정보
        private val _userInfo = MutableStateFlow(UserInfo())
        val userInfo: StateFlow<UserInfo> = _userInfo

        init {
            viewModelScope.launch {
                userPreferencesRepository.getUserInfo().collect {
                    if (it != null) {
                        _userInfo.value = it
                    }
                }
            }
        }

        private fun quitUser() {
            // 사용자 아이디
            viewModelScope.launch {
                deleteUserUseCase(_userInfo.value.userId).collect {
                    if (it != null) {
                        // 탈퇴 성공 처리
                        sendEffect(SettingEffect.DeleteUser)
                    }
                }
            }
        }

        override fun handleEvent(event: SettingEvent) {
            when (event) {
                is SettingEvent.QuitUser -> quitUser()
            }
        }
    }
