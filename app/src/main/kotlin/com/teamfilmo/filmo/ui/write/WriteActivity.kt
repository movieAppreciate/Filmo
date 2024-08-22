package com.teamfilmo.filmo.ui.write

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.teamfilmo.filmo.R
import com.teamfilmo.filmo.base.activity.BaseActivity
import com.teamfilmo.filmo.databinding.ActivityWriteBinding
import com.teamfilmo.filmo.ui.main.MainActivity
import com.teamfilmo.filmo.ui.write.movie.MovieSelectFragment
import com.teamfilmo.filmo.ui.write.report.WriteReportFragment
import com.teamfilmo.filmo.ui.write.thumbnail.ReportThumbnailFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WriteActivity : BaseActivity<ActivityWriteBinding, WriteViewModel, WriteEffect, WriteEvent>(
    ActivityWriteBinding::inflate,
) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.report_fragment_container_view, MovieSelectFragment.newInstance())
                .addToBackStack("select")
                .commit()
        }
    }

    override val viewModel: WriteViewModel by viewModels()

    fun navigateToWriteReportFragment(
        movieName: String,
        movieId: Int,
    ) {
        supportFragmentManager.beginTransaction()
            .add(R.id.report_fragment_container_view, WriteReportFragment.newInstance(movieName, movieId))
            .addToBackStack("write")
            .commit()
    }

    fun navigateToReportThumbnailFragment(
        movieName: String,
        movieId: Int,
    ) {
        supportFragmentManager.beginTransaction()
            .add(R.id.report_fragment_container_view, ReportThumbnailFragment.newInstance(movieName, movieId))
            .addToBackStack("thumbnail")
            .commit()
    }

    fun navigateToAllMovieReportFragment() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("NAVIGATE_TO_ALL_MOVIE_REPORT", true)
        setResult(RESULT_OK, intent)
        finish()
    }
}
