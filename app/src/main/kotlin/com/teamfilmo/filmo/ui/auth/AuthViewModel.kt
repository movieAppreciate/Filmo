package com.teamfilmo.filmo.ui.auth

import androidx.credentials.Credential
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import com.teamfilmo.filmo.data.remote.model.user.SignUpRequest
import com.teamfilmo.filmo.data.remote.model.user.UserInfo
import com.teamfilmo.filmo.data.source.UserTokenSource
import com.teamfilmo.filmo.domain.auth.GoogleLoginRequestUseCase
import com.teamfilmo.filmo.domain.auth.KakaoLoginRequestUseCase
import com.teamfilmo.filmo.domain.auth.NaverLoginRequestUseCase
import com.teamfilmo.filmo.domain.auth.SignUpUseCase
import com.teamfilmo.filmo.domain.repository.UserPreferencesRepository
import com.teamfilmo.filmo.domain.user.SaveUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber

@HiltViewModel
class AuthViewModel
    @Inject
    constructor(
        private val userPreferencesRepository: UserPreferencesRepository,
        private val saveUserInfoUseCase: SaveUserInfoUseCase,
        private val signUpUseCase: SignUpUseCase,
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

        private val _signUpResponse = MutableStateFlow<UserInfo?>(null)
        val signUpResponse = _signUpResponse.asStateFlow()

        // 회원 가입
        private fun requestSignUp(
            email: String,
            type: String,
        ) {
            viewModelScope.launch {
                signUpUseCase(SignUpRequest(email = email, type = type)).collect {
                    if (it != null) {
                        _signUpResponse.value = UserInfo(it.userId, it.nickname, it.roles)
                        // 회원 가입 성공 시 유저 정보 저장하기
                        saveUserInfoUseCase(_signUpResponse.value!!)
                        sendEffect(AuthEffect.SignUpSuccess)
                    } else {
                        sendEffect(AuthEffect.SignUpFailed)
                    }
                }
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

                try {
                    userPreferencesRepository.getUserInfo().collect {
                        if (it == null) {
                            // 유저 정보가 없는 경우 회원 가입으로 진행하기
                            requestSignUp(
                                email = email,
                                type = "kakao",
                            )
                        } else {
                            kakaoLoginRequestUseCase(email)
                                .onSuccess {
                                    Timber.d("kakao login success")
                                    userTokenSource.setUserToken(it.accessToken)
                                    sendEffect(AuthEffect.LoginSuccess)
                                }.onFailure {
                                    Timber.e("로그인 실패")
                                    requestSignUp(email, "kakao")
                                }
                        }
                    }
                } catch (e: Exception) {
                    Timber.e(e)
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

                try {
                    userPreferencesRepository.getUserInfo().collect {
                        if (it == null) {
                            // 유저 정보가 없는 경우 회원 가입으로 진행하기
                            requestSignUp(
                                email = email,
                                type = "naver",
                            )
                        } else {
                            naverLoginRequestUseCase(email)
                                .onSuccess {
                                    Timber.d("naver login success : $it")
                                    userTokenSource.setUserToken(it.accessToken)
                                    sendEffect(AuthEffect.LoginSuccess)
                                }.onFailure {
                                    Timber.e("naver login failed: ${it.message}")
                                    sendEffect(AuthEffect.LoginFailed)
                                }
                        }
                    }
                } catch (e: Exception) {
                    sendEffect(AuthEffect.LoginFailed)
                }
            }
        }

        private fun requestGoogleLogin(credential: Credential) {
            launch {
                try {
                    userPreferencesRepository.getUserInfo().collect {
                        if (it == null) {
                            // 유저 정보가 없는 경우 회원 가입으로 진행하기
                            val id = credential.data.getString("idToken")
                            requestSignUp(
                                email = "tjdgustjdan@gmail.com",
                                type = "google",
                            )
                        } else {
                            googleLoginRequestUseCase(credential)
                                .onSuccess {
                                    Timber.d("google login success")
                                    userTokenSource.setUserToken(it.accessToken)
                                    sendEffect(AuthEffect.LoginSuccess)
                                }.onFailure {
                                    Timber.e("google login failed: ${it.message}")
                                    sendEffect(AuthEffect.LoginFailed)
                                }
                        }
                    }
                } catch (e: Exception) {
                    Timber.e(e)
                    sendEffect(AuthEffect.LoginFailed)
                }
            }
        }
    }
