package com.teamfilmo.filmo.ui.report.tab

import androidx.fragment.app.Fragment
import com.teamfilmo.filmo.ui.report.all.AllMovieReportFragment
import com.teamfilmo.filmo.ui.report.follow.FollowingReportFragment

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
    FOLLOWING_REPORT(
        1,
        "팔로잉",
        FollowingReportFragment::newInstance,
    ),
    ;

    companion object {
        fun getTabInfoAt(
            position: Int,
        ): ReportTabInfo =
            entries.getOrNull(position) ?: throw IllegalArgumentException("Invalid position")
    }
}
