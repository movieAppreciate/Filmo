package com.teamfilmo.filmo.ui.user

import com.teamfilmo.filmo.base.event.BaseEvent

sealed class UserPageEvent : BaseEvent() {
    data class GetUserReportList(
        val targetId: String,
    ) : UserPageEvent()

    data object ClickFollow : UserPageEvent()
}
