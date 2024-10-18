package com.teamfilmo.filmo.ui.write

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.teamfilmo.filmo.R
import com.teamfilmo.filmo.base.activity.BaseActivity
import com.teamfilmo.filmo.databinding.ActivityWriteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WriteActivity : BaseActivity<ActivityWriteBinding, WriteViewModel, WriteEffect, WriteEvent>(
    ActivityWriteBinding::inflate,
) {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val navHostFragment =
            supportFragmentManager
                .findFragmentById(R.id.report_fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController

        if (savedInstanceState == null) {
            navController.navigate(R.id.movieSelectFragment)
        }
    }

    override val viewModel: WriteViewModel by viewModels()
}
