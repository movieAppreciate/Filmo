package com.teamfilmo.filmo.ui.auth

import com.teamfilmo.filmo.base.effect.BaseEffect

sealed interface AuthEffect : BaseEffect {
    data object NavigateToSignUp : AuthEffect

    data object LoginSuccess : AuthEffect

    data object LoginFailed : AuthEffect
}
