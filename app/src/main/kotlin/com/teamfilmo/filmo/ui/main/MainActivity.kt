package com.teamfilmo.filmo.ui.main

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.teamfilmo.filmo.R
import com.teamfilmo.filmo.base.activity.BaseActivity
import com.teamfilmo.filmo.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity :
    BaseActivity<ActivityMainBinding, MainViewModel, MainEffect, MainEvent>(
        ActivityMainBinding::inflate,
    ) {
    override val viewModel: MainViewModel by viewModels()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
    }

    override fun onBindLayout() {
        // 1. 먼저 NavHostFragment를 찾는다 (컨테이너)
        // NavHostFragment : 프래그먼트들이 들어가고 나가는 '그릇'
        val navHostFragment =
            supportFragmentManager
                .findFragmentById(R.id.main_fragment_container_view) as NavHostFragment

        // 2. NavHostFragment에서 NavController를 가져온다(리모콘)
        // NavController : 프로그먼트 간의 이동을 제어하는 역할
        navController = navHostFragment.navController

        // 3. BottomNavigationView와 연결해요
        // binding.navBar.setupWithNavController(navController)

        // 감상문 작성 과정에서 바텀바가 보이지 않도록 하기
        navController.addOnDestinationChangedListener { _, desitnation, _ ->
            binding.navBar.visibility =
                if (desitnation.id == R.id.writeReportFragment) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
        }

        binding.navBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.allMovieReportFragment -> {
                    if (navController.currentDestination?.id != R.id.allMovieReportFragment) {
                        navController.navigate(R.id.allMovieReportFragment)
                    }
                    true
                }

                R.id.movieSelectFragment -> {
                    if (navController.currentDestination?.id != R.id.movieSelectFragment) {
                        navController.navigate(R.id.movieSelectFragment)
                    }
                    true
                }
                R.id.myPageFragment -> {
                    // 만약 메인 페이지에 있다가 -> 마이 페이지로 들어온 경우
                    // 다시 홈 탭을 누르면 새 창말고 해당 탭으로 이동하기
                    if (navController.currentDestination?.id != R.id.myPageFragment) {
                        navController.navigate(R.id.myPageFragment)
                    }
                    true
                }

                else -> {
                    false
                }
            }
        }
    }
}
