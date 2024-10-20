package com.teamfilmo.filmo.ui.mypage

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.teamfilmo.filmo.base.fragment.BaseFragment
import com.teamfilmo.filmo.databinding.FragmentMyPageBinding
import com.teamfilmo.filmo.ui.mypage.adapter.MyPageAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding, MyPageViewModel, MyPageEffect, MyPageEvent>(
    FragmentMyPageBinding::inflate,
) {
    override val viewModel: MyPageViewModel by viewModels()
    private val navController by lazy { findNavController() }
    private val adapter by lazy { MyPageAdapter() }

    init {
//        lifecycleScope.launch {
//            launch {
//                viewModel.getFollowCount("tjdgustjdan@gmail.com")
//            }
//        }
    }

    override fun onBindLayout() {
        val args: MyPageFragmentArgs by navArgs()
        binding.myReportRecyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.getUserReportList(args.userId)
            viewModel.getUserInfo(args.userId)
            viewModel.checkIsFollow(args.userId)
        }

        binding.btnFollow.setOnClickListener {
            viewModel.handleEvent(MyPageEvent.ClickFollow)
        }

        viewLifecycleOwner.lifecycleScope.launch {
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

    override fun handleEffect(effect: MyPageEffect) {
        when (effect) {
            is MyPageEffect.IsNotFollow -> {
                binding.btnFollow.isSelected = false
            }
            is MyPageEffect.IsFollow -> {
                binding.btnFollow.isSelected = true
            }
            is MyPageEffect.SaveFollow -> {
                binding.btnFollow.isSelected = true
                Toast.makeText(context, "팔로우 완료!", Toast.LENGTH_SHORT).show()
            }
            is MyPageEffect.CancelFollow -> {
                binding.btnFollow.isSelected = false
                Toast.makeText(context, "팔로우 취소", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        fun newInstance(): MyPageFragment {
            val args = Bundle()

            val fragment = MyPageFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
