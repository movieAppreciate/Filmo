package com.teamfilmo.filmo.ui.setting

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.teamfilmo.filmo.base.fragment.BaseFragment
import com.teamfilmo.filmo.databinding.FragmentMyPageSettingBinding
import com.teamfilmo.filmo.ui.auth.AuthActivity
import com.teamfilmo.filmo.ui.widget.CustomDialog
import com.teamfilmo.filmo.ui.widget.ItemClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment :
    BaseFragment<FragmentMyPageSettingBinding, SettingViewModel, SettingEffect, SettingEvent>(
        FragmentMyPageSettingBinding::inflate,
    ) {
    override val viewModel: SettingViewModel by viewModels()
    private val navController by lazy { findNavController() }
    private val args: SettingFragmentArgs by navArgs()

    override fun handleEffect(effect: SettingEffect) {
        when (effect) {
            is SettingEffect.DeleteUser -> {
                // 탈퇴 성공 UI 처리 : 로그인 화면으로 이동
                val intent = Intent(context, AuthActivity::class.java)
                startActivity(intent)
            }
        }
    }

    // 신고  다이얼로그
    private fun showUserDeleteConfirmDialog() {
        val dialog =
            context?.let {
                CustomDialog(
                    button2Text = "탈퇴",
                    dialogMessage = "정말 탈퇴하시겠어요?",
                )
            }
        dialog?.show(parentFragmentManager, "UserDeleteDialog")
        dialog?.setButton2ClickListener(
            object : ItemClickListener {
                override fun onButton2Click() {
                    // todo : 감상문 신고 뷰모델 로직 호출
                    viewModel.handleEvent(SettingEvent.QuitUser)
                }

                override fun onButton1Click() {
                }
            },
        )
    }

    override fun onBindLayout() {
        binding.btnAnnounce.setOnClickListener {
            Toast.makeText(context, "서비스를 준비하고 있어요! 조금만 기다려주세요 :)", Toast.LENGTH_SHORT).show()
        }
        binding.btnQuestion.setOnClickListener {
            Toast.makeText(context, "서비스를 준비하고 있어요! 조금만 기다려주세요 :)", Toast.LENGTH_SHORT).show()
        }
        // 사용자 탈퇴
        binding.btnUserQuit.setOnClickListener {
            showUserDeleteConfirmDialog()
        }
        binding.txtUserName.text = args.nickName
        binding.btnBack.setOnClickListener {
            navController.popBackStack()
        }

        binding.btnTest.setOnClickListener {
            val myIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLSegGiD6MOPTNubow8cdN6t6PqAdSfl8A_TZoQaMeXGME0zTAA/viewform?usp=header"))
            startActivity(myIntent)
        }
    }
}
