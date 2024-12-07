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
        private fun checkUserToken() {
            userTokenSource
                .getUserToken()
                .onEach {
                    Timber.d("토큰 유무 검사 :$it")
                    if (it.isEmpty()) {
                        sendEffect(MainEffect.NavigateToLogin)
                    } else {
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
                        sendEffect(MainEffect.NavigateToLogin)
                    }
                }.launchIn(viewModelScope)
        }
    }
