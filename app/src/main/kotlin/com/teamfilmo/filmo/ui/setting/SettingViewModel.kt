package com.teamfilmo.filmo.ui.setting

import androidx.lifecycle.viewModelScope
import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import com.teamfilmo.filmo.domain.user.QuitUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class SettingViewModel
    @Inject
    constructor(
        private val quitUserUseCase: QuitUserUseCase,
    ) : BaseViewModel<SettingEffect, SettingEvent>() {
        private fun quitUser(userId: String) {
            // 사용자 아이디
            viewModelScope.launch {
                quitUserUseCase(userId).collect {
                    if (it.success) {
                        // 탈퇴 성공
                        Timber.d("success to quit user")
                    } else {
                        Timber.d("failed to quit user")
                    }
                }
            }
        }

        override fun handleEvent(event: SettingEvent) {
            when (event) {
                is SettingEvent.QuitUser -> {
                    quitUser("")
                }
            }
        }
    }
