package com.teamfilmo.filmo.ui.follow

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.teamfilmo.filmo.base.fragment.BaseFragment
import com.teamfilmo.filmo.databinding.FragmentFollowBinding
import com.teamfilmo.filmo.ui.follow.adapter.FollowPagerAdapter
import com.teamfilmo.filmo.ui.follow.tab.FollowTabInfo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowFragment :
    BaseFragment<FragmentFollowBinding, FollowViewModel, FollowEffect, FollowEvent>(
        FragmentFollowBinding::inflate,
    ) {
    private val args: FollowFragmentArgs by navArgs()
    override val viewModel: FollowViewModel by viewModels()
    private val navController by lazy { findNavController() }

    override fun handleEffect(effect: FollowEffect) {
    }

    override fun onBindLayout() {
        super.onBindLayout()
        val userId = args.userId

        // 뒤로 가기
        binding.btnBack.setOnClickListener {
            navController.popBackStack()
        }

        binding.followViewPager.adapter = userId?.let { FollowPagerAdapter(this, it) }
        binding.followViewPager.isUserInputEnabled = false
        binding.followViewPager.setCurrentItem(args.position, false)

        args.userId?.let { FollowEvent.GetFollowList(it) }?.let { viewModel.handleEvent(it) }
        TabLayoutMediator(binding.followTabLayout, binding.followViewPager) { tab, position ->
            tab.text = FollowTabInfo.getTabInfoAt(position).title
        }.attach()
    }
}
