package com.teamfilmo.filmo.ui.report

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.teamfilmo.filmo.R
import com.teamfilmo.filmo.base.activity.BaseActivity
import com.teamfilmo.filmo.databinding.ActivityReportBinding
import com.teamfilmo.filmo.ui.report.all.AllMovieReportFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportActivity : BaseActivity<ActivityReportBinding, ReportViewModel, ReportEffect, ReportEvent>(ActivityReportBinding::inflate) {
    override val viewModel: ReportViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.report_fragment_container_view, AllMovieReportFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onBindLayout() {
        super.onBindLayout()
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
}
