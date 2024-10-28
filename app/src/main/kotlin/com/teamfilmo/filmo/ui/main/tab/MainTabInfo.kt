package com.teamfilmo.filmo.ui.main.tab

import androidx.fragment.app.Fragment
import com.teamfilmo.filmo.ui.report.ReportFragment
import com.teamfilmo.filmo.ui.user.UserPageFragment
import com.teamfilmo.filmo.ui.write.select.MovieSelectFragment

enum class MainTabInfo(
    val fragment: () -> Fragment,
) {
    HOME(ReportFragment::newInstance),
    WRITE(MovieSelectFragment::newInstance),
    MY_PAGE(UserPageFragment::newInstance),
    ;

    companion object {
        fun getTabInfoAt(
            position: Int,
        ): MainTabInfo =
            MainTabInfo.entries.getOrNull(position) ?: throw IllegalArgumentException("Invalid position")
    }
}
