package com.teamfilmo.filmo.ui.main

import androidx.lifecycle.viewModelScope
import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import com.teamfilmo.filmo.data.source.UserTokenSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class MainViewModel
    @Inject
    constructor(
        private val userTokenSource: UserTokenSource,
    ) : BaseViewModel<MainEffect, MainEvent>() {
        init {
            checkUserToken()
        }

        private fun clear() {
            viewModelScope.launch {
                userTokenSource.clearUserToken()
            }
        }

        // Token 유무 검사
        // 1. 토큰 확인
        private fun checkUserToken() {
            userTokenSource
                .getUserToken()
                .onEach {
                    Timber.d("토큰 유무 검사 :$it")
                    if (it.isEmpty()) {
                        // 만약 토큰이 없을 경우
                        sendEffect(MainEffect.NavigateToLogin)
                    } else {
                        // 2. 토큰이 있을 경우 토큰 유효성 검증
                        isValidateUserToken()
                    }
                }.launchIn(viewModelScope)
        }

        // 토큰 유효성 검사
        private fun isValidateUserToken() {
            userTokenSource
                .getUserToken()
                .onEach {
                    if (it.isEmpty()) {
                        // 만약 토큰이 없다면
                        sendEffect(MainEffect.NavigateToLogin)
                    }
                }.launchIn(viewModelScope)
        }
    }
