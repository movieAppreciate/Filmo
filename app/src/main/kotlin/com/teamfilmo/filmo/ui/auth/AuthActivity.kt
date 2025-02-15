package com.teamfilmo.filmo.ui.auth

import android.content.Intent
import android.net.Uri
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.common.api.Api.Client
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthError
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.teamfilmo.filmo.R
import com.teamfilmo.filmo.base.activity.BaseActivity
import com.teamfilmo.filmo.base.util.showToast
import com.teamfilmo.filmo.databinding.ActivityAuthBinding
import com.teamfilmo.filmo.ui.main.MainActivity
import com.teamfilmo.filmo.util.click
import dagger.hilt.android.AndroidEntryPoint
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber

@AndroidEntryPoint
class AuthActivity : BaseActivity<ActivityAuthBinding, AuthViewModel, AuthEffect, AuthEvent>(ActivityAuthBinding::inflate) {
    override val viewModel: AuthViewModel by viewModels()
    private lateinit var credential: Credential

    override fun onBindLayout() {
        // 상태바 색깔 처리해주기
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        with(binding.googleLogin) {
            background = AppCompatResources.getDrawable(this@AuthActivity, R.drawable.bg_google)
            setButtonIcon(AppCompatResources.getDrawable(this@AuthActivity, R.drawable.ic_google))
            setButtonText("구글로 시작하기")
            setOnClickListener {
                onGoogleLogin()
            }
        }

        with(binding.naverLogin) {
            background = AppCompatResources.getDrawable(this@AuthActivity, R.drawable.bg_naver)
            setButtonIcon(AppCompatResources.getDrawable(this@AuthActivity, R.drawable.ic_naver))
            setButtonText("네이버로 시작하기")
            setButtonTextColor(AppCompatResources.getColorStateList(this@AuthActivity, R.color.white))
            setOnClickListener {
                onNaverLogin()
            }
        }

        with(binding.kakaoLogin) {
            background = AppCompatResources.getDrawable(this@AuthActivity, R.drawable.bg_kakao)
            setButtonIcon(AppCompatResources.getDrawable(this@AuthActivity, R.drawable.ic_kakao))
            setButtonText("카카오로 시작하기")
            setOnClickListener {
                onKakaoLogin()
            }
        }

        with(binding.notice) {
            text =
                buildSpannedString {
                    append("계정 생성 시 ")
                    click(
                        onClick = {
                            val uri = Uri.parse("https://enzfot.notion.site/a693da52f07e432ea365a30abee4e30c")
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            startActivity(intent)
                        },
                        color = this@AuthActivity.getColor(R.color.grey),
                    ) {
                        bold {
                            append("개인정보수집방침 및 이용약관")
                        }
                    }
                    append('\n')
                    append("(마케팅 정보 수신 동의 포함)에 동의하게 됩니다.")
                }
            movementMethod = LinkMovementMethod.getInstance()
        }
    }

    override fun handleEffect(effect: AuthEffect) {
        when (effect) {
            is AuthEffect.SignUpFailed -> {
                showToast("회원가입이 실패했어요! 계속되면 관리자에게 문의해주세요")
            }
            is AuthEffect.Existing -> {
                showToast("이미 등록된 계정이 있어요 ${effect.type} 계정으로 로그인합니다")
                when (effect.type) {
                    "google" -> onGoogleLogin()
                    "naver" -> onNaverLogin()
                    "kakao" -> onKakaoLogin()
                }
            }

            is AuthEffect.SignUpSuccess -> {
                showToast("회원가입 성공")
                when (effect.type) {
                    "google" -> {
                        viewModel.handleEvent(AuthEvent.RequestGoogleLogin(credential))
                    }
                    "naver" -> {
                        viewModel.handleEvent(AuthEvent.RequestNaverLogin)
                    }
                    "kakao" -> {
                        viewModel.handleEvent(AuthEvent.RequestKakaoLogin)
                    }
                }
            }

            AuthEffect.LoginSuccess -> {
                showToast("로그인 성공")
                val intent = Intent(this@AuthActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

            AuthEffect.LoginFailed -> {
                showToast("로그인 실패")
            }
        }
    }

    private suspend fun getCredential(
        credentialManager: CredentialManager,
        credentialRequest: GetCredentialRequest,
    ): Result<Credential> =
        runCatching {
            val response =
                credentialManager.getCredential(
                    request = credentialRequest,
                    context = this@AuthActivity,
                )
            response.credential
        }.onSuccess {
            credential = it
            viewModel.handleEvent(AuthEvent.RequestGoogleLogin(credential))
        }

    private fun onGoogleLogin() {
        Timber.d("onGoogleLogin")
        lifecycleScope.launch {
            val credentialOption =
                GetGoogleIdOption
                    .Builder()
                    .setFilterByAuthorizedAccounts(true)
                    .setAutoSelectEnabled(false)
                    .setServerClientId(getString(R.string.google_client_key))
                    .build()

            val signInWithGoogleOption: GetSignInWithGoogleOption =
                GetSignInWithGoogleOption
                    .Builder(getString(R.string.google_client_key))
                    .build()

            val credentialRequest =
                GetCredentialRequest
                    .Builder()
                    .addCredentialOption(credentialOption)
                    .build()

            val credentialManager = CredentialManager.create(this@AuthActivity)
            getCredential(
                credentialManager,
                credentialRequest,
            ).onFailure {
                when (it) {
                    is GetCredentialCancellationException -> {
                        showToast("로그인 취소")
                    }
                    is NoCredentialException -> {
                        showToast("계정을 확인 후 다시 시도해주세요")
                        val signInCredentialRequest =
                            GetCredentialRequest
                                .Builder()
                                .addCredentialOption(signInWithGoogleOption)
                                .build()

                        getCredential(credentialManager, signInCredentialRequest)
                            .onFailure {
                                when (it) {
                                    is GetCredentialCancellationException -> {
                                        showToast("계정 확인 후 다시 시도해주세요")
                                    }
                                    is NoCredentialException -> {
                                        showToast("기기에 계정을 등록하고 다시 시도해주세요")
                                    }
                                    else -> {
                                        showToast("잠시 후에 다시 시도해주세요")
                                    }
                                }
                            }
                    }
                    else -> {
                        Timber.d("google login failed :$it")
                    }
                }
            }
        }
    }

    private fun onNaverLogin() {
        lifecycleScope.launch {
            try {
                val token =
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
                            this@AuthActivity,
                            oauthLoginCallback,
                        )
                    }

                viewModel.handleEvent(AuthEvent.RequestNaverLogin)
            } catch (e: Exception) {
                Timber.e("failed naver login :${e.message}")
                showToast("로그인 취소")
            }
        }
    }

    private fun Continuation<OAuthToken>.resumeTokenOrException(
        token: OAuthToken?,
        error: Throwable?,
    ) {
        if (error != null) {
            resumeWithException(error)
        } else if (token != null) {
            resume(token)
        } else {
            resumeWithException(IllegalStateException("Can't receive kakao access token"))
        }
    }

    // 카카오톡으로 로그인 시도
    private suspend fun loginWithKakaotalk(): OAuthToken =
        suspendCancellableCoroutine { continuation ->
            continuation.invokeOnCancellation {
                when (it) {
                    is ClientError -> {
                        showToast("로그인 취소")
                    }
                }
            }
            UserApiClient.instance.loginWithKakaoTalk(this@AuthActivity) { token, error ->
                continuation.resumeTokenOrException(token, error)
            }
        }

    // 카카오 계정으로 로그인 시도
    private suspend fun loginWithKakaoAccount(): OAuthToken =
        suspendCancellableCoroutine { continuation ->
            continuation.invokeOnCancellation {
                when (it) {
                    is Client -> {
                        showToast("로그인 취소")
                    }
                }
            }
            UserApiClient.instance.loginWithKakaoAccount(this@AuthActivity) { token, error ->
                continuation.resumeTokenOrException(token, error)
            }
        }

    private fun onKakaoLogin() {
        lifecycleScope.launch {
            try {
                // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
                if (UserApiClient.instance.isKakaoTalkLoginAvailable(this@AuthActivity)) {
                    try {
                        loginWithKakaotalk()
                    } catch (error: Throwable) {
                        loginWithKakaoAccount()
                    }
                } else {
                    loginWithKakaoAccount()
                }
                viewModel.handleEvent(AuthEvent.RequestKakaoLogin)
            } catch (e: ClientError) {
                when (e.reason) {
                    ClientErrorCause.Cancelled -> showToast("로그인 취소")
                    ClientErrorCause.Unknown -> showToast("Unknown")
                    else -> {}
                }
            } catch (e: AuthError) {
                when (e.statusCode) {
                    302 -> loginWithKakaoAccount()
                }
            }
        }
    }
}
