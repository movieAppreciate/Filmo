package com.teamfilmo.filmo.ui.auth

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import com.teamfilmo.filmo.base.effect.NoEffect
import com.teamfilmo.filmo.base.event.NoEvent
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
        private val credentialRequest: GetCredentialRequest,
        private val naverLoginRequestUseCase: NaverLoginRequestUseCase,
        private val googleLoginRequestUseCase: GoogleLoginRequestUseCase,
        private val kakaoLoginRequestUseCase: KakaoLoginRequestUseCase,
    ) : BaseViewModel<NoEffect, NoEvent>() {
        fun requestKakaoLogin(context: Context) {
            launch {
                runCatching {
                    suspendCancellableCoroutine { continuation ->
                        val kakaoLoginCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                            if (error != null) {
                                Timber.w("kakao login error ${error.message}")
                                continuation.resumeWithException(error)
                            } else if (token != null) {
                                Timber.i("kakao login success, ${token.accessToken}")
                                continuation.resume(token)
                            }
                        }

                        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                            try {
                                UserApiClient.instance.loginWithKakaoTalk(context, callback = kakaoLoginCallback)
                            } catch (error: Throwable) {
                                if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                                    continuation.resumeWithException(error)
                                } else {
                                    UserApiClient.instance.loginWithKakaoAccount(context, callback = kakaoLoginCallback)
                                }
                            }
                        } else {
                            UserApiClient.instance.loginWithKakaoAccount(context, callback = kakaoLoginCallback)
                        }
                    }
                }.getOrThrow()

                val email =
                    runCatching {
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
                    }.getOrThrow()

                kakaoLoginRequestUseCase(email)
                    .onSuccess {
                        userTokenSource.setUserToken(it.accessToken)
                        Timber.d("kakao login success")
                    }
                    .onFailure {
                        Timber.e("kakao login failed: ${it.message}")
                        Log.d("로그인 실패", it.message.toString())
                    }
            }
        }

        fun requestNaverLogin(context: Context) {
            launch {
                val token =
                    runCatching {
                        suspendCancellableCoroutine { continuation ->
                            val oauthLoginCallback =
                                object : OAuthLoginCallback {
                                    override fun onSuccess() {
                                        Timber.d("naver login success, token: ${NaverIdLoginSDK.getAccessToken()}")
                                        continuation.resume(NaverIdLoginSDK.getAccessToken().toString())
                                    }

                                    override fun onFailure(
                                        httpStatus: Int,
                                        message: String,
                                    ) {
                                        val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                                        val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                                        Timber.e("naver login failed, code: $errorCode, description: $errorDescription")
                                        continuation.resumeWithException(Exception("naver login failed, code: $errorCode, description: $errorDescription"))
                                    }

                                    override fun onError(
                                        errorCode: Int,
                                        message: String,
                                    ) {
                                        onFailure(errorCode, message)
                                    }
                                }

                            continuation.invokeOnCancellation {
                                Timber.d("naver login canceled, ${it?.message}")
                            }

                            NaverIdLoginSDK.authenticate(
                                context,
                                oauthLoginCallback,
                            )
                        }
                    }.getOrThrow()

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
                        userTokenSource.setUserToken(it.accessToken)
                        Timber.d("naver login success")
                    }
                    .onFailure {
                        Timber.e("naver login failed: ${it.message}")
                        Log.d("로그인 실패", it.message.toString())
                    }
            }
        }

        fun requestGoogleLogin(context: Context) {
            launch {
                val credentialManager = CredentialManager.create(context)

                val response =
                    runCatching {
                        credentialManager.getCredential(
                            request = credentialRequest,
                            context = context,
                        )
                    }.getOrThrow()

                val credential = response.credential
                googleLoginRequestUseCase(credential)
                    .onSuccess {
                        userTokenSource.setUserToken(it.accessToken)
                        Timber.d("google login success")
                    }
                    .onFailure {
                        Timber.e("google login failed: ${it.message}")
                        Log.d("로그인 실패", it.message.toString())
                    }
            }
        }
    }
