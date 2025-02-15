package com.teamfilmo.filmo.ui.write.select

import com.teamfilmo.filmo.base.effect.BaseEffect

sealed interface MovieSelectEffect : BaseEffect {
    data object SearchMovie : MovieSelectEffect
}
