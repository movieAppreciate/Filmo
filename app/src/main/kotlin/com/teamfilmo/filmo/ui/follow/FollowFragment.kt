package com.teamfilmo.filmo.ui.follow

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.tabs.TabLayoutMediator
import com.teamfilmo.filmo.base.fragment.BaseFragment
import com.teamfilmo.filmo.databinding.FragmentFollowBinding
import com.teamfilmo.filmo.ui.follow.adapter.FollowPagerAdapter
import com.teamfilmo.filmo.ui.follow.tab.FollowTabInfo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FollowFragment : BaseFragment<FragmentFollowBinding, FollowViewModel, FollowEffect, FollowEvent>(
    FragmentFollowBinding::inflate,
) {
    override val viewModel: FollowViewModel by viewModels()

    override fun onBindLayout() {
        super.onBindLayout()
        binding.followViewPager.adapter = FollowPagerAdapter(this)
        binding.followViewPager.isUserInputEnabled = false

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
            }
        }
        TabLayoutMediator(binding.followTabLayout, binding.followViewPager) { tab, position ->
            tab.text = FollowTabInfo.getTabInfoAt(position).title
        }.attach()
    }
}
