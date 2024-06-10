package com.teamfilmo.filmo.ui.mypage

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.teamfilmo.filmo.base.fragment.BaseFragment
import com.teamfilmo.filmo.databinding.FragmentMyPageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding, MyPageViewModel, MyPageEffect, MyPageEvent>(
    FragmentMyPageBinding::inflate,
) {
    override val viewModel: MyPageViewModel by viewModels()

    override fun onBindLayout() {
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
