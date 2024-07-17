package com.teamfilmo.filmo.ui.report.all

import com.teamfilmo.filmo.base.effect.BaseEffect

sealed interface AllMovieReportEffect : BaseEffect {
    data object RegistLike : AllMovieReportEffect

    data object CancelLike : AllMovieReportEffect
}
