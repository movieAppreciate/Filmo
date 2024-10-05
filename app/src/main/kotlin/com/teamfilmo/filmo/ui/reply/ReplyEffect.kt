package com.teamfilmo.filmo.ui.reply

import com.teamfilmo.filmo.base.effect.BaseEffect

sealed interface ReplyEffect : BaseEffect {
    data object SaveReply : ReplyEffect

    data object SaveSubReply : ReplyEffect

    data class DeleteReply(val position: Int) : ReplyEffect

    data class DeleteSubReply(val subReplyId: String) : ReplyEffect
}
