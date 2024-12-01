package com.teamfilmo.filmo.ui.setting

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.teamfilmo.filmo.base.fragment.BaseFragment
import com.teamfilmo.filmo.databinding.FragmentMyPageSettingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment :
    BaseFragment<FragmentMyPageSettingBinding, SettingViewModel, SettingEffect, SettingEvent>(
        FragmentMyPageSettingBinding::inflate,
    ) {
    override val viewModel: SettingViewModel by viewModels()
    private val navController by lazy { findNavController() }
    private val args: SettingFragmentArgs by navArgs()

    override fun onBindLayout() {
        // 사용자 탈퇴
        binding.btnUserQuit.setOnClickListener {
            viewModel.handleEvent(SettingEvent.QuitUser)
        }
        binding.txtUserName.text = args.nickName
        binding.btnBack.setOnClickListener {
            navController.popBackStack()
        }
    }
}
