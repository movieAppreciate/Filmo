package com.teamfilmo.filmo.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.teamfilmo.filmo.R
import com.teamfilmo.filmo.base.activity.BaseActivity
import com.teamfilmo.filmo.databinding.ActivityMainBinding
import com.teamfilmo.filmo.ui.auth.AuthActivity
import com.teamfilmo.filmo.ui.write.WriteActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel, MainEffect, MainEvent>(
    ActivityMainBinding::inflate,
) {
    override val viewModel: MainViewModel by viewModels()
    private lateinit var navController: NavController

    private val requestLogin =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                viewModel.handleEvent(MainEvent.CheckUserToken)
            } else {
                finish()
            }
        }

    fun updateNavigationBar(selectedItemId: Int) {
        binding.navBar.selectedItemId = selectedItemId
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navHostFragment =
            supportFragmentManager
                .findFragmentById(R.id.main_fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController
        binding.navBar.setupWithNavController(navController)

        if (intent.getBooleanExtra("NAVIGATE_TO_ALL_MOVIE_REPORT", false)) {
            navController.navigate(R.id.allMovieReportFragment)
        } else if (intent.getBooleanExtra("NAVIGATE_TO_MOVIE_DETAIL", false)) {
            Timber.d("")
            navController.navigate(R.id.movieDetailFragment)
        }
    }

    override fun onStart() {
        super.onStart()
        binding.navBar.selectedItemId = R.id.allMovieReportFragment
    }

    override fun onBindLayout() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        binding.navBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.allMovieReportFragment -> {
                    if (navController.currentDestination?.id != R.id.allMovieReportFragment) {
                        navController.navigate(R.id.allMovieReportFragment)
                    }
                    return@setOnItemSelectedListener true
                }
                R.id.myPageFragment -> {
                    if (navController.currentDestination?.id != R.id.myPageFragment) {
                        Timber.d("마이페이지 클릭")
                        navController.navigate(R.id.myPageFragment)
                    }
                    return@setOnItemSelectedListener true
                }

                R.id.writeActivity -> {
                    val intent = Intent(this, WriteActivity::class.java)
                    Timber.d("감상문 작성 클릭")
                    startActivity(intent)
                    return@setOnItemSelectedListener true
                }
                else -> {
                    return@setOnItemSelectedListener false
                }
            }
        }
    }

    override fun handleEffect(effect: MainEffect) {
        when (effect) {
            is MainEffect.NavigateToLogin -> {
                val intent = Intent(this, AuthActivity::class.java)
                requestLogin.launch(intent)
            }
            else -> {}
        }
    }
}
