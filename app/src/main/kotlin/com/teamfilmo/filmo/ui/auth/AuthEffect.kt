package com.teamfilmo.filmo.ui.auth

import com.teamfilmo.filmo.base.effect.BaseEffect

sealed interface AuthEffect : BaseEffect {
    data object Existing : AuthEffect

    data class SignUpSuccess(
        val type: String,
    ) : AuthEffect

    data object SignUpFailed : AuthEffect

    data object NavigateToSignUp : AuthEffect

    data object LoginSuccess : AuthEffect

    data object LoginFailed : AuthEffect
}
