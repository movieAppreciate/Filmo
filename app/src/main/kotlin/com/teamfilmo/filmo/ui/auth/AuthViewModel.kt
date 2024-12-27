package com.teamfilmo.filmo.ui.auth

import androidx.credentials.Credential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import com.teamfilmo.filmo.data.remote.model.user.SignUpRequest
import com.teamfilmo.filmo.data.remote.model.user.SignUpResult
import com.teamfilmo.filmo.data.remote.model.user.UserInfo
import com.teamfilmo.filmo.data.source.UserTokenSource
import com.teamfilmo.filmo.domain.auth.GoogleLoginRequestUseCase
import com.teamfilmo.filmo.domain.auth.KakaoLoginRequestUseCase
import com.teamfilmo.filmo.domain.auth.NaverLoginRequestUseCase
import com.teamfilmo.filmo.domain.auth.SignUpUseCase
import com.teamfilmo.filmo.domain.user.SaveUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber

@HiltViewModel
class AuthViewModel
    @Inject
    constructor(
        private val saveUserInfoUseCase: SaveUserInfoUseCase,
        private val signUpUseCase: SignUpUseCase,
        private val userTokenSource: UserTokenSource,
        private val naverLoginRequestUseCase: NaverLoginRequestUseCase,
        private val googleLoginRequestUseCase: GoogleLoginRequestUseCase,
        private val kakaoLoginRequestUseCase: KakaoLoginRequestUseCase,
    ) : BaseViewModel<AuthEffect, AuthEvent>() {
        private var googleCredential: Credential? = null

        override fun handleEvent(event: AuthEvent) {
            when (event) {
                is AuthEvent.RequestGoogleLogin -> {
                    requestGoogleLogin(event.credential)
                    googleCredential = event.credential
                }
                is AuthEvent.RequestNaverLogin -> requestNaverLogin()
                is AuthEvent.RequestKakaoLogin -> requestKakaoLogin()
            }
        }

        private val _signUpResponse = MutableStateFlow<UserInfo?>(null)
        val signUpResponse = _signUpResponse.asStateFlow()

        // 토큰이 없기 때문에 로그인 화면이 뜬 것이다! 따라서 로그인 요청이 들어오면 회원가입 후 로그인이 실행되도록 한다.
        // 다음부터는 저장된 토큰을 통해 자동으로 로그인이 구현된다.

        // 회원 가입
        private suspend fun requestSignUp(
            email: String,
            type: String,
        ) {
            coroutineScope {
                signUpUseCase(SignUpRequest(email = email, type = type)).collect {
                    if (it != null) {
                        when (it) {
                            is SignUpResult.Success -> {
                                // 회원 가입 성공 시 유저 정보 저장하기
                                saveUserInfoUseCase(
                                    UserInfo(
                                        userId = it.response.userId,
                                        type = it.response.type,
                                        roles = it.response.roles,
                                        nickName = it.response.nickname,
                                    ),
                                )
                                // 해당 계정으로 로그인 하기
                                sendEffect(AuthEffect.SignUpSuccess(type))
                            }
                            is SignUpResult.Existing -> {
                                // 이미 다른 로그인으로 등록된 경우
                                // 그 방식으로 로그인 처리
                                sendEffect(AuthEffect.Existing(it.response.type))
                            }
                            is SignUpResult.Error -> {
                                Timber.d("회원 가입 실패")
                            }
                        }
                    } else {
                        Timber.d("회원가입 UseCase 형태 null? : $it")
                    }
                }
            }
        }

        private fun requestKakaoLogin() {
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
                    kakaoLoginRequestUseCase(email)
                        .onSuccess {
                            Timber.d("kakao login success")
                            // 로그인 성공 시 access token 저장하기
                            userTokenSource.setUserToken(it.accessToken)
                            sendEffect(AuthEffect.LoginSuccess)
                        }.onFailure {
                            Timber.e("로그인 실패")
                            // sendEffect(AuthEffect.LoginFailed)
                            requestSignUp(email, "kakao")
                        }
                } catch (e: Exception) {
                }
            }
        }

        private fun requestNaverLogin() {
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
                    naverLoginRequestUseCase(email)
                        .onSuccess {
                            Timber.d("naver login success : $it")
                            userTokenSource.setUserToken(it.accessToken)
                            sendEffect(AuthEffect.LoginSuccess)
                        }.onFailure {
                            Timber.e("naver login failed: ${it.message}")
//                            sendEffect(AuthEffect.LoginFailed)
                            requestSignUp(email, "naver")
                        }
                } catch (e: Exception) {
                    Timber.e("Google sign-in process failed: ${e.message}")
                }
            }
        }

        private fun requestGoogleLogin(credential: Credential) {
            launch {
                try {
                    val googleIdTokenCredential =
                        GoogleIdTokenCredential.createFrom(
                            credential.data,
                        )
                    googleLoginRequestUseCase(credential)
                        .onSuccess {
                            Timber.d("google login success")
                            userTokenSource.setUserToken(it.accessToken)
                            sendEffect(AuthEffect.LoginSuccess)
                        }.onFailure {
                            Timber.e("google login failed: ${it.message}")
                            // sendEffect(AuthEffect.LoginFailed)
                            requestSignUp(googleIdTokenCredential.id, "google")
                        }
                } catch (e: Exception) {
                    Timber.e("Google sign-in process failed: ${e.message}")
                    sendEffect(AuthEffect.SignUpFailed)
                }
            }
        }
    }
