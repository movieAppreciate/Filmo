package com.teamfilmo.filmo.ui.splash

import com.teamfilmo.filmo.base.effect.BaseEffect

sealed interface SplashEffect : BaseEffect {
    data object NavigateToLogin : SplashEffect

    data object NavigateToMain : SplashEffect
}
