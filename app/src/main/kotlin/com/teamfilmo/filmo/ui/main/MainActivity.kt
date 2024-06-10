package com.teamfilmo.filmo.ui.main

import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.teamfilmo.filmo.R
import com.teamfilmo.filmo.base.activity.BaseActivity
import com.teamfilmo.filmo.databinding.ActivityMainBinding
import com.teamfilmo.filmo.ui.auth.AuthActivity
import com.teamfilmo.filmo.ui.main.adapter.MainPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

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

    override fun onBindLayout() {
        binding.viewPager.adapter = MainPagerAdapter(this)
        binding.viewPager.isUserInputEnabled = false
        binding.navBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> binding.viewPager.currentItem = 0
                R.id.write -> binding.viewPager.currentItem = 1
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
        }
    }
}
