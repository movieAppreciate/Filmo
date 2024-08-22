package com.teamfilmo.filmo.ui.write.report

import com.teamfilmo.filmo.base.effect.BaseEffect

sealed interface WriteReportEffect : BaseEffect {
    data object NavigateToMain : WriteReportEffect
}
