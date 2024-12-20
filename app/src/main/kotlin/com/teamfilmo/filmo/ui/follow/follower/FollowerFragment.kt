package com.teamfilmo.filmo.ui.follow.follower

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.teamfilmo.filmo.base.fragment.BaseFragment
import com.teamfilmo.filmo.databinding.FragmentFollowerBinding
import com.teamfilmo.filmo.ui.follow.adapter.FollowerRVAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FollowerFragment :
    BaseFragment<FragmentFollowerBinding, FollowerViewModel, FollowerEffect, FollowerEvent>(
        FragmentFollowerBinding::inflate,
    ) {
    private val adapter = FollowerRVAdapter()
    override val viewModel: FollowerViewModel by viewModels()

    override fun onBindLayout() {
        val userId = arguments?.getString("userId")
        binding.followerRecyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                if (userId != null) {
                    viewModel.getFollowerList(userId)
                }
            }
            launch {
                viewModel.pagingFollowerData.collectLatest {
                    adapter.submitData(it)
                }
            }
        }
    }

    companion object {
        fun newInstance(userId: String): FollowerFragment {
            val fragment = FollowerFragment()
            fragment.arguments =
                Bundle().apply {
                    putString("userId", userId)
                }
            return fragment
        }
    }
}
