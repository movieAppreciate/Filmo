package com.teamfilmo.filmo.ui.mypage

import com.teamfilmo.filmo.base.effect.BaseEffect

sealed interface MyPageEffect : BaseEffect {
    data object IsFollow : MyPageEffect

    data object IsNotFollow : MyPageEffect

    data object SaveFollow : MyPageEffect

    data object CancelFollow : MyPageEffect
}
