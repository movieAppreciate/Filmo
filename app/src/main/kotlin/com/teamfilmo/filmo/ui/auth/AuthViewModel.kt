package com.teamfilmo.filmo.ui.auth

import androidx.credentials.Credential
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import com.teamfilmo.filmo.data.source.UserTokenSource
import com.teamfilmo.filmo.domain.auth.GoogleLoginRequestUseCase
import com.teamfilmo.filmo.domain.auth.KakaoLoginRequestUseCase
import com.teamfilmo.filmo.domain.auth.NaverLoginRequestUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber

@HiltViewModel
class AuthViewModel
    @Inject
    constructor(
        private val userTokenSource: UserTokenSource,
        private val naverLoginRequestUseCase: NaverLoginRequestUseCase,
        private val googleLoginRequestUseCase: GoogleLoginRequestUseCase,
        private val kakaoLoginRequestUseCase: KakaoLoginRequestUseCase,
    ) : BaseViewModel<AuthEffect, AuthEvent>() {
        override fun handleEvent(event: AuthEvent) {
            when (event) {
                is AuthEvent.RequestGoogleLogin -> requestGoogleLogin(event.credential)
                is AuthEvent.RequestNaverLogin -> requestNaverLogin(event.token)
                is AuthEvent.RequestKakaoLogin -> requestKakaoLogin(event.token)
            }
        }

        private fun requestKakaoLogin(token: OAuthToken) {
            launch {
                val email =
                    suspendCancellableCoroutine { continuation ->
                        UserApiClient.instance.me { user, error ->
                            if (error != null) {
                                Timber.e("kakao login user info failed ${error.message}")
                                continuation.resumeWithException(error)
                            } else if (user != null) {
                                Timber.i("kakao login user info success, email: ${user.kakaoAccount?.email}")
                                user.kakaoAccount?.email?.let { continuation.resume(it) } ?: continuation.resumeWithException(Exception("kakao login failed: email is null"))
                            }
                        }
                    }

                kakaoLoginRequestUseCase(email)
                    .onSuccess {
                        Timber.d("kakao login success")
                        userTokenSource.setUserToken(it.accessToken)
                        sendEffect(AuthEffect.LoginSuccess)
                    }
                    .onFailure {
                        Timber.e("kakao login failed: ${it.message}")
                        sendEffect(AuthEffect.LoginFailed)
                    }
            }
        }

        private fun requestNaverLogin(token: String) {
            launch {
                val email =
                    runCatching {
                        suspendCancellableCoroutine { continuation ->
                            val profileCallback =
                                object : NidProfileCallback<NidProfileResponse> {
                                    override fun onError(
                                        errorCode: Int,
                                        message: String,
                                    ) {
                                        onFailure(errorCode, message)
                                    }

                                    override fun onFailure(
                                        httpStatus: Int,
                                        message: String,
                                    ) {
                                        val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                                        val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                                        Timber.e("naver login failed, code: $errorCode, description: $errorDescription")
                                        continuation.resumeWithException(
                                            Exception("naver login failed, code: $errorCode, description: $errorDescription"),
                                        )
                                    }

                                    override fun onSuccess(result: NidProfileResponse) {
                                        val email = result.profile?.email
                                        if (email != null) {
                                            continuation.resume(email)
                                        } else {
                                            continuation.resumeWithException(Exception("naver login failed: email is null"))
                                        }
                                    }
                                }

                            NidOAuthLogin().callProfileApi(profileCallback)
                        }
                    }.getOrThrow()

                naverLoginRequestUseCase(email)
                    .onSuccess {
                        Timber.d("naver login success")
                        userTokenSource.setUserToken(it.accessToken)
                        sendEffect(AuthEffect.LoginSuccess)
                    }
                    .onFailure {
                        Timber.e("naver login failed: ${it.message}")
                        sendEffect(AuthEffect.LoginFailed)
                    }
            }
        }

        private fun requestGoogleLogin(credential: Credential) {
            launch {
                googleLoginRequestUseCase(credential)
                    .onSuccess {
                        Timber.d("google login success")
                        userTokenSource.setUserToken(it.accessToken)
                        sendEffect(AuthEffect.LoginSuccess)
                    }
                    .onFailure {
                        Timber.e("google login failed: ${it.message}")
                        sendEffect(AuthEffect.LoginFailed)
                    }
            }
        }
    }
