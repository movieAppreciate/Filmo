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

        binding.txtCountFollow.setOnClickListener {
            val action =
                MyPageFragmentDirections.navigateToFollowFragment(
                    position = 0,
                    userId = viewModel.userInfo.value.userId,
                    isMyPage = true,
                )
            navController.navigate(action)
        }

        binding.txtCountFollowing.setOnClickListener {
            val action =
                MyPageFragmentDirections.navigateToFollowFragment(
                    position = 1,
                    userId = viewModel.userInfo.value.userId,
                )
            navController.navigate(action)
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
