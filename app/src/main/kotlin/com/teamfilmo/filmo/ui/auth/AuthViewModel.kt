package com.teamfilmo.filmo.ui.auth

import android.content.Context
import android.util.Log
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import com.teamfilmo.filmo.domain.auth.MakeGoogleLoginRequestUseCase
import com.teamfilmo.filmo.domain.auth.MakeKakaoLoginRequestUseCase
import com.teamfilmo.filmo.domain.auth.MakeNaverLoginRequestUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel
    @Inject
    constructor(
        private val makeNaverLoginRequestUseCase: MakeNaverLoginRequestUseCase,
        private val makeGoogleLoginRequestUseCase: MakeGoogleLoginRequestUseCase,
        private val makeKakaoLoginRequestUseCase: MakeKakaoLoginRequestUseCase,
    ) : ViewModel() {
        @Inject
        lateinit var credentialRequest: GetCredentialRequest

        fun requestKakaoLogin(context: Context) {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                    if (error != null) {
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }
                        UserApiClient.instance.loginWithKakaoAccount(context, callback = kakaoLogincallback)
                    } else if (token != null) {
                        Log.d("kakao login success", "${token.accessToken}")
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(context, callback = kakaoLogincallback)
            }
        }

        private fun getUserEmail() {
            UserApiClient.instance.me { user, error ->
                if (error != null) {
                    Log.d("kakao login user info failed", error.message.toString())
                } else if (user != null) {
                    Log.d("kakao login user info success", "email : ${user.kakaoAccount?.email}")
                    viewModelScope.launch {
                        user.kakaoAccount?.email?.let { makeKakaoLoginRequestUseCase(it) }
                    }
                }
            }
        }

        private val kakaoLogincallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.d("kakao login error", error.toString())
            } else if (token != null) {
                getUserEmail()
                Log.i("kakao login success ", "${token.accessToken}")
            }
        }

        fun requestNaverLogin(context: Context) {
            NaverIdLoginSDK.authenticate(context, oauthLoginCallback)
        }

        private val oauthLoginCallback =
            object : OAuthLoginCallback {
                override fun onSuccess() {
                    // 접근 토큰
                    Log.d("naver login success", NaverIdLoginSDK.getAccessToken().toString())

                    // todo : 뷰 모델에서 login/signUp api 호출
                    NidOAuthLogin().callProfileApi(profileCallback)
                }

                override fun onFailure(
                    httpStatus: Int,
                    message: String,
                ) {
                    val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                    val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                    Log.d("naver login error", "errorCode:$errorCode, errorDesc:$errorDescription")
                }

                override fun onError(
                    errorCode: Int,
                    message: String,
                ) {
                    onFailure(errorCode, message)
                }
            }

        // 로그인 유저 정보 가져오기 위한 콜백
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
                    Log.d("naver login  failed", "code = $errorCode \n description = $errorDescription")
                }

                override fun onSuccess(result: NidProfileResponse) {
                    val email = result.profile?.email
                    if (email != null) {
                        Log.d("naver login success : email ", email)
                        viewModelScope.launch {
                            makeNaverLoginRequestUseCase(email)
                        }
                        // login api에 email을 넣어주기
                    }
                }
            }

        fun requestGoogleLogin(context: Context) {
            val credentialManager = CredentialManager.create(context)
            viewModelScope.launch {
                runCatching {
                    credentialManager.getCredential(
                        request = credentialRequest,
                        context = context,
                    )
                }
                    .onSuccess {
                        val credential = it.credential
                        googleLogin(credential)
                    }
                    .onFailure {
                        Log.d("google login failed", it.message.toString())
                    }
            }
        }

        private fun googleLogin(credential: Credential) {
            viewModelScope.launch {
                makeGoogleLoginRequestUseCase(credential)
            }
        }
    }
