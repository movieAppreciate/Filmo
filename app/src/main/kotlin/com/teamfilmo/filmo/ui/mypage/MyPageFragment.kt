package com.teamfilmo.filmo.ui.mypage

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.teamfilmo.filmo.R
import com.teamfilmo.filmo.base.fragment.BaseFragment
import com.teamfilmo.filmo.databinding.FragmentMyPageBinding
import com.teamfilmo.filmo.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding, MyPageViewModel, MyPageEffect, MyPageEvent>(
    FragmentMyPageBinding::inflate,
) {
    override val viewModel: MyPageViewModel by viewModels()

    init {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.getFollowCount("tjdgustjdan@gmail.com")
                }
            }
        }
    }

    override fun onBindLayout() {
        (activity as? MainActivity)?.updateNavigationBar(R.id.my_page)

        /*
        todo 감상문 정보 보이게 하기
         */

        // todo : 내 아이디 넘기기
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
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
    }

    override fun handleEffect(effect: MyPageEffect) {
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
