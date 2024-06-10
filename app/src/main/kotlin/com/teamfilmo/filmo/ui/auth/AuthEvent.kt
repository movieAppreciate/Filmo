package com.teamfilmo.filmo.ui.auth

import androidx.credentials.Credential
import com.kakao.sdk.auth.model.OAuthToken
import com.teamfilmo.filmo.base.event.BaseEvent

sealed class AuthEvent : BaseEvent() {
    data class RequestGoogleLogin(
        val credential: Credential,
    ) : AuthEvent()

    data class RequestNaverLogin(
        val token: String,
    ) : AuthEvent()

    data class RequestKakaoLogin(
        val token: OAuthToken,
    ) : AuthEvent()
}
