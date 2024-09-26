package com.teamfilmo.filmo.ui.reply

import com.teamfilmo.filmo.base.effect.BaseEffect

sealed interface ReplyEffect : BaseEffect {
    data object SaveReply : ReplyEffect
}
