package com.teamfilmo.filmo.ui.auth

import android.content.Intent
import android.net.Uri
import android.text.method.LinkMovementMethod
import androidx.activity.viewModels
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import com.teamfilmo.filmo.R
import com.teamfilmo.filmo.base.activity.BaseActivity
import com.teamfilmo.filmo.databinding.ActivityAuthBinding
import com.teamfilmo.filmo.util.click
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : BaseActivity<ActivityAuthBinding>(ActivityAuthBinding::inflate) {
    private val viewModel: AuthViewModel by viewModels()

    override fun initViews() {
        super.initViews()

        binding.googleLogin.setOnClickListener {
            viewModel.requestGoogleLogin(this@AuthActivity)
        }

        binding.naverLogin.setOnClickListener {
            viewModel.requestNaverLogin(this@AuthActivity)
        }

        binding.kakaoLogin.setOnClickListener {
            viewModel.requestKakaoLogin(this@AuthActivity)
        }

        binding.notice.text =
            buildSpannedString {
                append("계정 생성 시 ")
                click(
                    onClick = {
                        val uri = Uri.parse("https://axiomatic-tie-306.notion.site/7b56ec5868664b36b8a9f7e599c288e5?pvs=4")
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
        binding.notice.movementMethod = LinkMovementMethod.getInstance()
    }
}
