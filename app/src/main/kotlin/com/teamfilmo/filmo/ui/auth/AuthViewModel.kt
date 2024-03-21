package com.teamfilmo.filmo.ui.auth

import android.content.Context
import android.util.Log
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import com.teamfilmo.filmo.domain.auth.MakeGoogleLoginRequestUseCase
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
    ) : ViewModel() {
        @Inject
        lateinit var credentialRequest: GetCredentialRequest

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
                    Log.d("네이버 로그인 profile failed", "code = $errorCode \n description = $errorDescription")
                }

                override fun onSuccess(result: NidProfileResponse) {
                    val email = result.profile?.email
                    if (email != null) {
                        Log.d("네이버 로그인 profile  결과 => Id ", email)
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
