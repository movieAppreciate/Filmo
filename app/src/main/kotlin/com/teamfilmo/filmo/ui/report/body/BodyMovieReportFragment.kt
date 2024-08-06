package com.teamfilmo.filmo.ui.report.body

import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.teamfilmo.filmo.base.fragment.BaseFragment
import com.teamfilmo.filmo.databinding.FragmentBodyMovieReportBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BodyMovieReportFragment : BaseFragment<FragmentBodyMovieReportBinding, BodyMovieReportViewModel, BodyMovieReportEffect, BodyMovieReportEvent>(
    FragmentBodyMovieReportBinding::inflate,
) {
    override val viewModel: BodyMovieReportViewModel by viewModels()

    override fun onBindLayout() {
        super.onBindLayout()

        viewModel.viewModelScope.launch {
        }
    }

    override fun handleEffect(effect: BodyMovieReportEffect) {
        super.handleEffect(effect)
    }
}
