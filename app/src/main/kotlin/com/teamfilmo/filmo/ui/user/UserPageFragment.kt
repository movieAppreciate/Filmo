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
import timber.log.Timber

@AndroidEntryPoint
class UserPageFragment :
    BaseFragment<FragmentUserPageBinding, UserPageViewModel, UserPageEffect, UserPageEvent>(
        FragmentUserPageBinding::inflate,
    ) {
    override val viewModel: UserPageViewModel by viewModels()
    private val navController by lazy { findNavController() }
    private val adapter by lazy { UserPageAdapter() }

    init {
//        lifecycleScope.launch {
//            launch {
//                viewModel.getFollowCount("tjdgustjdan@gmail.com")
//            }
//        }
    }

    override fun onBindLayout() {
        val args: UserPageFragmentArgs by navArgs()
        binding.myReportRecyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.handleEvent(UserPageEvent.GetUserReportList(args.userId))
            viewModel.checkIsFollow(args.userId)
        }

        binding.btnFollow.setOnClickListener {
            viewModel.handleEvent(UserPageEvent.ClickFollow)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.userInfo.collect {
                    binding.txtUserName.text = it.nickname
                }
            }
            launch {
                viewModel.userReportList.collect {
                    Timber.d("사용자 게시글 : $it")
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
