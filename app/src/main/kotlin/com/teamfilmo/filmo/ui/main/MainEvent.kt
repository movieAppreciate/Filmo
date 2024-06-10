package com.teamfilmo.filmo.ui.main

import com.teamfilmo.filmo.base.event.BaseEvent

sealed class MainEvent : BaseEvent() {
    data object CheckUserToken : MainEvent()
}
