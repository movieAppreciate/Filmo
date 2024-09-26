package com.teamfilmo.filmo.ui.report.tab

import androidx.fragment.app.Fragment
import com.teamfilmo.filmo.ui.report.all.AllMovieReportFragment

enum class ReportTabInfo(
    val position: Int,
    val title: String,
    val fragment: () -> Fragment,
) {
    ALL_MOVIE_REPORT(
        0,
        "전체",
        AllMovieReportFragment::newInstance,
    ),
    ;

    companion object {
        fun getTabInfoAt(
            position: Int,
        ): ReportTabInfo =
            entries.getOrNull(position) ?: throw IllegalArgumentException("Invalid position")
    }
}
