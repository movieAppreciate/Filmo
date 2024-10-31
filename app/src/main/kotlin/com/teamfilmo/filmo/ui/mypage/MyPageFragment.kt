package com.teamfilmo.filmo.ui.mypage

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.teamfilmo.filmo.R
import com.teamfilmo.filmo.base.fragment.BaseFragment
import com.teamfilmo.filmo.databinding.FragmentMyPageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment :
    BaseFragment<FragmentMyPageBinding, MyPageViewModel, MyPageEffect, MyPageEvent>(
        FragmentMyPageBinding::inflate,
    ) {
    override val viewModel: MyPageViewModel by viewModels()
    private val navController by lazy { findNavController() }

    override fun onBindLayout() {
        super.onBindLayout()
        binding.btnBack.setOnClickListener {
            navController.popBackStack()
        }
        viewModel.followInfo.value.apply {
            binding.txtCountFollow.text = this.countFollower.toString()
            binding.txtCountFollowing.text = this.countFollowing.toString()
        }
        binding.btnSetting.setOnClickListener {
            navController.navigate(R.id.settingFragment)
        }
    }
}
