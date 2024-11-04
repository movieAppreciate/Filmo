package com.teamfilmo.filmo.ui.follow.following

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.teamfilmo.filmo.base.fragment.BaseFragment
import com.teamfilmo.filmo.databinding.FragmentFollowingBinding
import com.teamfilmo.filmo.ui.follow.adapter.FollowingRVAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class FollowingFragment :
    BaseFragment<FragmentFollowingBinding, FollowingViewModel, FollowingEffect, FollowingEvent>(
        FragmentFollowingBinding::inflate,
    ) {
    override val viewModel: FollowingViewModel by viewModels()
    private val adapter = FollowingRVAdapter()

    override fun onBindLayout() {
        binding.followingRecyclerView.adapter = adapter
        val userId = arguments?.getString("userId")

        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                if (userId != null) {
                    Timber.d("following list 얻어오기 ")
                    viewModel.getFollowingList(userId)
                }
            }

            launch {
                viewModel.followingList.collect {
                    adapter.setFollowers(it)
                }
            }
        }
    }

    companion object {
        fun newInstance(userId: String): FollowingFragment {
            val fragment = FollowingFragment()
            fragment.arguments =
                Bundle().apply {
                    putString("userId", userId)
                }
            return fragment
        }
    }
}
