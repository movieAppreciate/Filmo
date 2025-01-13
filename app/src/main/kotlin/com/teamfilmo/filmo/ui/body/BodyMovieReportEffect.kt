package com.teamfilmo.filmo.ui.body

import com.teamfilmo.filmo.base.effect.BaseEffect

sealed interface BodyMovieReportEffect : BaseEffect {
    data object BlockSuccess : BodyMovieReportEffect

    data object ComplaintSuccess : BodyMovieReportEffect

    data object CancelFollow : BodyMovieReportEffect

    data object SaveFollow : BodyMovieReportEffect
}
