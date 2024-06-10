package com.teamfilmo.filmo.ui.search

import androidx.activity.viewModels
import com.teamfilmo.filmo.base.activity.BaseActivity
import com.teamfilmo.filmo.databinding.ActivitySearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : BaseActivity<ActivitySearchBinding, SearchViewModel, SearchEffect, SearchEvent>(
    ActivitySearchBinding::inflate,
) {
    override val viewModel: SearchViewModel by viewModels()

    override fun onBindLayout() {
    }

    override fun handleEffect(effect: SearchEffect) {
    }
}
