package com.teamfilmo.filmo.ui.search

import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.teamfilmo.filmo.R
import com.teamfilmo.filmo.base.activity.BaseActivity
import com.teamfilmo.filmo.databinding.ActivitySearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : BaseActivity<ActivitySearchBinding, SearchViewModel, SearchEffect, SearchEvent>(
    ActivitySearchBinding::inflate,
) {
    override val viewModel: SearchViewModel by viewModels()

    override fun onBindLayout() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

    override fun handleEffect(effect: SearchEffect) {
    }
}
