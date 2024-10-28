package com.teamfilmo.filmo.ui.setting

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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

    override fun onBindLayout() {
        binding.btnBack.setOnClickListener {
            navController.popBackStack()
        }
    }
}
