package com.teamfilmo.filmo.ui.setting

import com.teamfilmo.filmo.base.event.BaseEvent

sealed class SettingEvent : BaseEvent() {
    data object QuitUser : SettingEvent()
}
