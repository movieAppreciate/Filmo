package com.teamfilmo.filmo.ui.user

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.teamfilmo.filmo.base.fragment.BaseFragment
import com.teamfilmo.filmo.databinding.FragmentUserPageBinding
import com.teamfilmo.filmo.ui.user.adapter.UserPageAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserPageFragment :
    BaseFragment<FragmentUserPageBinding, UserPageViewModel, UserPageEffect, UserPageEvent>(
        FragmentUserPageBinding::inflate,
    ) {
    override val viewModel: UserPageViewModel by viewModels()
    private val navController by lazy { findNavController() }
    private val adapter by lazy { UserPageAdapter() }

    override fun onBindLayout() {
        val args: UserPageFragmentArgs by navArgs()
        with(binding) {
            myReportRecyclerView.adapter = adapter

            btnBack.setOnClickListener {
                navController.popBackStack()
            }

            btnFollow.setOnClickListener {
                viewModel.handleEvent(UserPageEvent.ClickFollow)
            }

            txtCountFollowing.setOnClickListener {
                val action =
                    UserPageFragmentDirections.navigateToFollowFragment(
                        userId = args.userId,
                        position = 1,
                    )
                navController.navigate(action)
            }

            txtCountFollow.setOnClickListener {
                val action =
                    UserPageFragmentDirections.navigateToFollowFragment(
                        userId = args.userId,
                        position = 0,
                    )
                navController.navigate(action)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.handleEvent(UserPageEvent.GetUserReportList(args.userId))
            viewModel.checkIsFollow(args.userId)

            launch {
                viewModel.userInfo.collect {
                    binding.txtUserName.text = it.nickname
                }
            }
            launch {
                viewModel.userReportList.collect {
                    adapter.setMyReportList(it)
                }
            }
            launch {
                viewModel.followerCount.collect {
                    binding.txtCountFollow.text = it.toString()
                }
            }
            launch {
                viewModel.followingCount.collect {
                    binding.txtCountFollowing.text = it.toString()
                }
            }
        }
    }

    override fun handleEffect(effect: UserPageEffect) {
        when (effect) {
            is UserPageEffect.IsNotFollow -> {
                binding.btnFollow.isSelected = false
            }
            is UserPageEffect.IsFollow -> {
                binding.btnFollow.isSelected = true
            }
            is UserPageEffect.SaveFollow -> {
                binding.btnFollow.isSelected = true
                Toast.makeText(context, "팔로우 완료!", Toast.LENGTH_SHORT).show()
            }
            is UserPageEffect.CancelFollow -> {
                binding.btnFollow.isSelected = false
                Toast.makeText(context, "팔로우 취소", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        fun newInstance(): UserPageFragment {
            val args = Bundle()

            val fragment = UserPageFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
