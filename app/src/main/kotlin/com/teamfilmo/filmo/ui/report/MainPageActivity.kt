package com.teamfilmo.filmo.ui.report

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayout
import com.teamfilmo.filmo.R
import com.teamfilmo.filmo.databinding.ActivityMainPageBinding
import com.teamfilmo.filmo.ui.notification.NotificationActivity
import com.teamfilmo.filmo.ui.search.SearchActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainPageActivity :
    AppCompatActivity() {
    private lateinit var binding: ActivityMainPageBinding
    private val reportViewModel by viewModels<ReportViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        lifecycleScope.launch {
            reportViewModel.requestReport()
            reportViewModel.getBookmarkResponse()
        }

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(R.id.fragmentArea, MovieReportFragment())
            }
        }

        binding.tabButton.addOnTabSelectedListener(
            object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    val fragmentTransaction = supportFragmentManager.beginTransaction()
                    when (tab?.text) {
                        "전체" -> {
                            fragmentTransaction.replace(R.id.fragmentArea, MovieReportFragment()).commit()
                        }
                        "팔로잉" -> {
                            fragmentTransaction.replace(R.id.fragmentArea, FollowingReportFragment()).commit()
                        }
                    }
                }

                override fun onTabUnselected(p0: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            },
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.top_app_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.search -> {
                val intent = Intent(this, SearchActivity::class.java)
                startActivity(intent)
                true
            }

            R.id.notification -> {
                val intent = Intent(this, NotificationActivity::class.java)
                startActivity(intent)
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
}
