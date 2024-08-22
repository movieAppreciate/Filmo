package com.teamfilmo.filmo.ui.write.thumbnail

import com.teamfilmo.filmo.base.effect.BaseEffect

sealed interface ReportThumbnailEffect : BaseEffect {
    data object GetMoviePoster : ReportThumbnailEffect

    data object GetMovieBackground : ReportThumbnailEffect
}
