package com.teamfilmo.filmo.ui.main

import com.teamfilmo.filmo.base.effect.BaseEffect

sealed interface MainEffect : BaseEffect {
    data object NavigateToLogin : MainEffect
}
