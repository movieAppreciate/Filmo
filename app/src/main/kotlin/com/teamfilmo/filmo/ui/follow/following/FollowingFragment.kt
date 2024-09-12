package com.teamfilmo.filmo.ui.follow.following

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.teamfilmo.filmo.base.fragment.BaseFragment
import com.teamfilmo.filmo.databinding.FragmentFollowingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowingFragment : BaseFragment<FragmentFollowingBinding, FollowingViewModel, FollowingEffect, FollowingEvent>(
    FragmentFollowingBinding::inflate,
) {
    override val viewModel: FollowingViewModel by viewModels()

    companion object {
        fun newInstance(): FollowingFragment {
            val args = Bundle()
            val fragment = FollowingFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
