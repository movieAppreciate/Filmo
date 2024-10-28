package com.teamfilmo.filmo.ui.user

import com.teamfilmo.filmo.base.effect.BaseEffect

sealed interface UserPageEffect : BaseEffect {
    data object IsFollow : UserPageEffect

    data object IsNotFollow : UserPageEffect

    data object SaveFollow : UserPageEffect

    data object CancelFollow : UserPageEffect
}
