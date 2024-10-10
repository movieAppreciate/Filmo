package com.teamfilmo.filmo.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.commit
import com.teamfilmo.filmo.R
import com.teamfilmo.filmo.base.activity.BaseActivity
import com.teamfilmo.filmo.databinding.ActivityMainBinding
import com.teamfilmo.filmo.ui.auth.AuthActivity
import com.teamfilmo.filmo.ui.body.BodyMovieReportFragment
import com.teamfilmo.filmo.ui.main.adapter.MainPagerAdapter
import com.teamfilmo.filmo.ui.movie.MovieDetailFragment
import com.teamfilmo.filmo.ui.reply.ReplyFragment
import com.teamfilmo.filmo.ui.report.ReportFragment
import com.teamfilmo.filmo.ui.write.WriteActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel, MainEffect, MainEvent>(
    ActivityMainBinding::inflate,
) {
    override val viewModel: MainViewModel by viewModels()

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
        if (intent.getBooleanExtra("NAVIGATE_TO_ALL_MOVIE_REPORT", false)) {
            binding.viewPager.currentItem = 0
        } else if (intent.getBooleanExtra("NAVIGATE_TO_MOVIE_DETAIL", false)) {
            Timber.d("")
            supportFragmentManager.commit {
            }
        }

        binding.navBar.selectedItemId = R.id.home
    }

    override fun onStart() {
        super.onStart()
        binding.navBar.selectedItemId = R.id.home
    }

    override fun onBindLayout() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        binding.viewPager.adapter = MainPagerAdapter(this)
        binding.viewPager.isUserInputEnabled = false
        binding.navBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    binding.viewPager.currentItem = 0
                }
                R.id.write -> {
                    val intent = Intent(this, WriteActivity::class.java)
                    startActivity(intent)
                }
                R.id.my_page -> binding.viewPager.currentItem = 2
                else -> {
                    return@setOnItemSelectedListener false
                }
            }
            return@setOnItemSelectedListener true
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

    fun navigateToReportFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.main_fragment_container_view, ReportFragment.newInstance())
            .addToBackStack(null)
            .commit()
        binding.viewPager.visibility = View.VISIBLE
        binding.mainFragmentContainerView.visibility = View.GONE
    }

    fun navigateToDetailMovieFragment(movieID: Int) {
        supportFragmentManager.beginTransaction()
            .add(R.id.main_fragment_container_view, MovieDetailFragment.newInstance(movieID))
            .addToBackStack(null)
            .commit()
        binding.viewPager.visibility = View.GONE
        binding.mainFragmentContainerView.visibility = View.VISIBLE
    }

    fun navigateToBodyFragment(
        reportId: String,
    ) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragment_container_view, BodyMovieReportFragment.newInstance(reportId))
            .addToBackStack(null)
            .commit()
        binding.viewPager.visibility = View.GONE
        binding.mainFragmentContainerView.visibility = View.VISIBLE
    }

    fun navigateToReplyFragment(
        reportId: String,
    ) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragment_container_view, ReplyFragment.newInstance(reportId))
            .addToBackStack(null)
            .commit()

        binding.mainFragmentContainerView.visibility = View.VISIBLE
    }
}
