package com.teamfilmo.filmo.ui.follow.tab

import androidx.fragment.app.Fragment
import com.teamfilmo.filmo.ui.follow.follower.FollowerFragment
import com.teamfilmo.filmo.ui.follow.following.FollowingFragment

enum class FollowTabInfo(
    val position: Int,
    val title: String,
    val fragment: (String) -> Fragment,
) {
    Follower(
        0,
        "팔로워",
        { userId ->
            FollowerFragment.newInstance(userId)
        },
    ),
    Following(
        1,
        "팔로잉",
        { userId -> FollowingFragment.newInstance(userId) },
    ),
    ;

    companion object {
        fun getTabInfoAt(
            position: Int,
        ): FollowTabInfo =
            entries.getOrNull(position) ?: throw IllegalArgumentException("Invalid position")
    }
}
