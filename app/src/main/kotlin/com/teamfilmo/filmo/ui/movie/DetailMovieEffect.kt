package com.teamfilmo.filmo.ui.movie

import com.teamfilmo.filmo.base.effect.BaseEffect

sealed interface DetailMovieEffect : BaseEffect {
    data object ShowDetailMovie : DetailMovieEffect
}
