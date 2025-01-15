package com.teamfilmo.filmo.ui.auth

import androidx.credentials.Credential
import com.teamfilmo.filmo.base.event.BaseEvent

sealed class AuthEvent : BaseEvent() {
    data class RequestGoogleLogin(
        val credential: Credential,
    ) : AuthEvent()

    data object RequestNaverLogin : AuthEvent()

    data object RequestKakaoLogin : AuthEvent()
}
