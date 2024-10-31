package com.teamfilmo.filmo.ui.follow

import com.teamfilmo.filmo.base.event.BaseEvent

sealed class FollowEvent : BaseEvent() {
    data class GetFollowList(
        val userId: String,
    ) : FollowEvent()
}
