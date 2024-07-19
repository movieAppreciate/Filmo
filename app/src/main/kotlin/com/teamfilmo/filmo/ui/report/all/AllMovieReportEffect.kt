package com.teamfilmo.filmo.ui.report.all

import com.teamfilmo.filmo.base.effect.BaseEffect

sealed interface AllMovieReportEffect : BaseEffect {
    data class RegistLike(val reportId: String) : AllMovieReportEffect

    data class CancelLike(val reportId: String) : AllMovieReportEffect
}
