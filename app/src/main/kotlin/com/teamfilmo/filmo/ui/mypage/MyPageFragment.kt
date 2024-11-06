package com.teamfilmo.filmo.ui.mypage

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.teamfilmo.filmo.R
import com.teamfilmo.filmo.base.fragment.BaseFragment
import com.teamfilmo.filmo.databinding.FragmentMyPageBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyPageFragment :
    BaseFragment<FragmentMyPageBinding, MyPageViewModel, MyPageEffect, MyPageEvent>(
        FragmentMyPageBinding::inflate,
    ) {
    override val viewModel: MyPageViewModel by viewModels()
    private val navController by lazy { findNavController() }

    override fun onBindLayout() {
        super.onBindLayout()

        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.followInfo.collect {
                    binding.txtCountFollow.text = it.countFollower.toString()
                    binding.txtCountFollowing.text = it.countFollowing.toString()
                }
            }
            launch {
                viewModel.userInfo.collect {
                    binding.txtUserName.text = it.nickname
                }
            }
        }
        with(binding) {
            btnSetting.setOnClickListener {
                navController.navigate(R.id.settingFragment)
            }

            btnBack.setOnClickListener {
                navController.popBackStack()
            }

            txtCountFollow.setOnClickListener {
                val action =
                    MyPageFragmentDirections.navigateToFollowFragment(
                        position = 0,
                        userId = viewModel.userInfo.value.userId,
                    )
                navController.navigate(action)
            }

            txtCountFollowing.setOnClickListener {
                val action =
                    MyPageFragmentDirections.navigateToFollowFragment(
                        position = 1,
                        userId = viewModel.userInfo.value.userId,
                    )
                navController.navigate(action)
            }
        }
    }
}
