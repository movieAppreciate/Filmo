package com.teamfilmo.filmo.ui.body

import com.teamfilmo.filmo.base.effect.BaseEffect

sealed interface BodyMovieReportEffect : BaseEffect {
    data object RegistLike : BodyMovieReportEffect

    data object CancelLike : BodyMovieReportEffect

    data object BlockSuccess : BodyMovieReportEffect

    data object ComplaintSuccess : BodyMovieReportEffect

    data object CancelFollow : BodyMovieReportEffect

    data object SaveFollow : BodyMovieReportEffect

    data object ShowReport : BodyMovieReportEffect

    data object ShowMovieInfo : BodyMovieReportEffect

    data object ShowMovieContent : BodyMovieReportEffect
}
