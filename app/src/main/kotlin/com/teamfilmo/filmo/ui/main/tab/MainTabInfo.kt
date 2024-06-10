package com.teamfilmo.filmo.ui.main.tab

import androidx.fragment.app.Fragment
import com.teamfilmo.filmo.ui.mypage.MyPageFragment
import com.teamfilmo.filmo.ui.report.ReportFragment
import com.teamfilmo.filmo.ui.write.WriteFragment

enum class MainTabInfo(val fragment: () -> Fragment) {
    HOME(ReportFragment::newInstance),
    WRITE(WriteFragment::newInstance),
    MY_PAGE(MyPageFragment::newInstance),
    ;

    companion object {
        fun getTabInfoAt(
            position: Int,
        ): MainTabInfo =
            MainTabInfo.entries.getOrNull(position) ?: throw IllegalArgumentException("Invalid position")
    }
}
