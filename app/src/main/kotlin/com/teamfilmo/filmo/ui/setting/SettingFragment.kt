package com.teamfilmo.filmo.ui.setting

import android.content.Intent
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
                // // todo 탈퇴 성공 UI 처리 : 로그인 화면으로 이동?
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
        dialog?.setItemClickListener(
            object : ItemClickListener {
                override fun onClick() {
                    // todo : 감상문 신고 뷰모델 로직 호출
                    viewModel.handleEvent(SettingEvent.QuitUser)
                }
            },
        )
    }

    override fun onBindLayout() {
        // 사용자 탈퇴
        binding.btnUserQuit.setOnClickListener {
            showUserDeleteConfirmDialog()
        }
        binding.txtUserName.text = args.nickName
        binding.btnBack.setOnClickListener {
            navController.popBackStack()
        }
    }
}
