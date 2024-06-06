package com.teamfilmo.filmo.ui.auth

import android.content.Intent
import android.net.Uri
import android.text.method.LinkMovementMethod
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat.startActivity
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.lifecycle.lifecycleScope
import com.teamfilmo.filmo.R
import com.teamfilmo.filmo.base.activity.BaseActivity
import com.teamfilmo.filmo.data.source.UserTokenSource
import com.teamfilmo.filmo.databinding.ActivityAuthBinding
import com.teamfilmo.filmo.ui.report.MainPageActivity
import com.teamfilmo.filmo.util.click
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthActivity
    @Inject
    constructor() :
    BaseActivity<ActivityAuthBinding>(ActivityAuthBinding::inflate) {
        private val viewModel: AuthViewModel by viewModels()

        @Inject
        lateinit var userTokenSource: UserTokenSource

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

            // todo : viewmodel에서 처리하도록 수정
            lifecycleScope.launch {
                if (userTokenSource.getUserToken().firstOrNull()?.isNotEmpty() == true) {
                    val intent = Intent(this@AuthActivity, MainPageActivity::class.java)
                    Toast.makeText(this@AuthActivity, "로그인 성공", Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
