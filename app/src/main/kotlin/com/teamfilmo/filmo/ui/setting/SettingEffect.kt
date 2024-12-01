package com.teamfilmo.filmo.ui.setting

import com.teamfilmo.filmo.base.effect.BaseEffect

sealed interface SettingEffect : BaseEffect {
    data object DeleteUser : SettingEffect
}
