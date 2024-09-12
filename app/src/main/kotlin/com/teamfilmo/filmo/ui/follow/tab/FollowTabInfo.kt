package com.teamfilmo.filmo.ui.follow.tab

import androidx.fragment.app.Fragment
import com.teamfilmo.filmo.ui.follow.follower.FollowerFragment
import com.teamfilmo.filmo.ui.follow.following.FollowingFragment

enum class FollowTabInfo(
    val position: Int,
    val title: String,
    val fragment: () -> Fragment,
) {
    Follower(
        0,
        "팔로워",
        FollowerFragment::newInstance,
    ),
    Following(
        1,
        "팔로잉",
        FollowingFragment::newInstance,
    ),
    ;

    companion object {
        fun getTabInfoAt(
            position: Int,
        ): FollowTabInfo =
            entries.getOrNull(position) ?: throw IllegalArgumentException("Invalid position")
    }
}
