package com.teamfilmo.filmo.ui.modify

import com.teamfilmo.filmo.base.effect.BaseEffect

sealed interface UpdateReportEffect : BaseEffect {
    data object UpdateSuccess : UpdateReportEffect
}
