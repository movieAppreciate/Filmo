package com.teamfilmo.filmo.ui.follow.follower

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.teamfilmo.filmo.base.fragment.BaseFragment
import com.teamfilmo.filmo.databinding.FragmentFollowerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowerFragment : BaseFragment<FragmentFollowerBinding, FollowerViewModel, FollowerEffect, FollowerEvent>(
    FragmentFollowerBinding::inflate,
) {
    override val viewModel: FollowerViewModel by viewModels()

    companion object {
        fun newInstance(): FollowerFragment {
            val args = Bundle()
            val fragment = FollowerFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
