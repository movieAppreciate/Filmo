package com.teamfilmo.filmo.ui.write.movie

import com.teamfilmo.filmo.base.effect.BaseEffect

sealed interface MovieSelectEffect : BaseEffect {
    data object SearchMovie : MovieSelectEffect
}
