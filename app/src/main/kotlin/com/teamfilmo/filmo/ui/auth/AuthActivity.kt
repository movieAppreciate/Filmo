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
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.kakao.sdk.auth.model.OAuthToken
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
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber

@AndroidEntryPoint
class AuthActivity : BaseActivity<ActivityAuthBinding, AuthViewModel, AuthEffect, AuthEvent>(ActivityAuthBinding::inflate) {
    override val viewModel: AuthViewModel by viewModels()

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
                when (effect.type) {
                    "google" -> onGoogleLogin()
                    "naver" -> onNaverLogin()
                    "kakao" -> onKakaoLogin()
                }
            }

            is AuthEffect.SignUpSuccess -> {
                Timber.d("회원가입 성공!")
                when (effect.type) {
                    "google" -> onGoogleLogin()
                    "naver" -> onNaverLogin()
                    "kakao" -> onKakaoLogin()
                }
            }

            AuthEffect.LoginSuccess -> {
                lifecycleScope.launch {
                    showToast("로그인 성공")
                    val intent = Intent(this@AuthActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
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
            val credential = it
            viewModel.handleEvent(AuthEvent.RequestGoogleLogin(credential))
        }

    private fun onGoogleLogin() {
        lifecycleScope.launch {
            var credentialManager = CredentialManager.create(this@AuthActivity)

            val credentialOption =
                GetGoogleIdOption
                    .Builder()
                    .setFilterByAuthorizedAccounts(false)
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

            getCredential(
                credentialManager,
                credentialRequest,
            ).onFailure {
                when (it) {
                    is GetCredentialCancellationException -> {
                        Timber.d("GetCredentialCancellationException: $it")
                        showToast("로그인 취소")
                    }
                    is NoCredentialException -> {
                        val signInCredentialRequest =
                            GetCredentialRequest
                                .Builder()
                                .addCredentialOption(signInWithGoogleOption)
                                .build()

                        getCredential(credentialManager, signInCredentialRequest)
                            .onFailure {
                                when (it) {
                                    is GetCredentialCancellationException -> {
                                        Timber.d("GetCredentialCancellationException: $it")
                                        showToast("로그인 취소")
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

                viewModel.handleEvent(AuthEvent.RequestNaverLogin(token))
            } catch (e: Exception) {
                showToast("로그인 취소")
            }
        }
    }

    private fun onKakaoLogin() {
        lifecycleScope.launch {
            try {
                val token =
                    suspendCancellableCoroutine { continuation ->
                        val kakaoLoginCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                            if (error != null) {
                                // 코루틴의 실행 흐름에서 에러 발생 시 해당 에러를 코루틴의 예외로 전파한다.
                                continuation.resumeWithException(error)
                            } else if (token != null) {
                                Timber.i("kakao login success, ${token.accessToken}")
                                // 성공하면 토큰을 전달한다.
                                continuation.resume(token)
                            }
                        }

                        continuation.invokeOnCancellation {
                            when (it) {
                                // todo : 카카오 로그인 창을 닫은 경우 처리
                                is ClientError -> {
                                    Timber.d("kakao login canceled, ${it?.message}")
                                }
                            }
                        }

                        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this@AuthActivity)) {
                            try {
                                UserApiClient.instance.loginWithKakaoTalk(this@AuthActivity, callback = kakaoLoginCallback)
                            } catch (error: Throwable) {
                                if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                                    continuation.resumeWithException(error)
                                } else {
                                    Timber.d("kakao login else")
                                    UserApiClient.instance.loginWithKakaoAccount(this@AuthActivity, callback = kakaoLoginCallback)
                                }
                            }
                        } else {
                            UserApiClient.instance.loginWithKakaoAccount(this@AuthActivity, callback = kakaoLoginCallback)
                        }
                    }

                viewModel.handleEvent(AuthEvent.RequestKakaoLogin(token))
            } catch (e: ClientError) {
                // 이후 전파한 예외를 잡아서 처리해주면 된다
                when (e.reason) {
                    ClientErrorCause.Cancelled -> {
                        showToast("로그인 취소")
                    }
                    else -> {}
                }
            }
        }
    }
}
