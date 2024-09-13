package com.teamfilmo.filmo.ui.body

import com.teamfilmo.filmo.base.effect.BaseEffect

sealed interface BodyMovieReportEffect : BaseEffect {
    data object ShowReport : BodyMovieReportEffect

    data object ShowMovieInfo : BodyMovieReportEffect
}
