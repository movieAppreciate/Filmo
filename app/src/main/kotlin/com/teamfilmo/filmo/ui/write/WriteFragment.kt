package com.teamfilmo.filmo.ui.write

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.teamfilmo.filmo.base.fragment.BaseFragment
import com.teamfilmo.filmo.databinding.FragmentWriteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WriteFragment : BaseFragment<FragmentWriteBinding, WriteViewModel, WriteEffect, WriteEvent>(
    FragmentWriteBinding::inflate,
) {
    override val viewModel: WriteViewModel by viewModels()

    override fun onBindLayout() {
    }

    override fun handleEffect(effect: WriteEffect) {
    }

    companion object {
        fun newInstance(): WriteFragment {
            val args = Bundle()

            val fragment = WriteFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
