package com.teamfilmo.filmo.ui.auth
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import com.google.android.material.snackbar.Snackbar
import com.navercorp.nid.NaverIdLoginSDK
import com.teamfilmo.filmo.R
import com.teamfilmo.filmo.base.BaseActivity
import com.teamfilmo.filmo.databinding.ActivityMainBinding
import com.teamfilmo.filmo.util.click
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity() :
    BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.googleLogin.setOnClickListener {
            try {
                authViewModel.requestGoogleLogin(this@AuthActivity)
            } catch (e: Exception) {
                Log.e("Google Login Error", e.message.toString())
                Snackbar.make(binding.root, "로그인 실패: ${e.message}", Snackbar.LENGTH_SHORT).show()
            }
        }
        binding.naverLogin.setOnClickListener {
            try {
                authViewModel.requestNaverLogin(this@AuthActivity)
            } catch (e: Exception) {
                Log.d("naver login error", e.message.toString())
            }
        }

        binding.kakaoLogin.setOnClickListener {
            authViewModel.requestKakaoLogin(this)
        }
    }

    override fun init() {
        super.init()
        NaverIdLoginSDK.initialize(this, getString(R.string.naver_client_id), getString(R.string.naver_client_secret), getString(R.string.naver_client_name))
        NaverIdLoginSDK.showDevelopersLog(true)

        binding.notice.text =
            buildSpannedString {
                append("계정 생성 시 ")
                click(
                    onClick = {
                        val uri = Uri.parse("https://www.google.com")
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
    }
}
