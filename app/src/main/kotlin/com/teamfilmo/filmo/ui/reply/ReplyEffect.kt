package com.teamfilmo.filmo.ui.reply

import com.teamfilmo.filmo.base.effect.BaseEffect

sealed interface ReplyEffect : BaseEffect {
    data class SaveLike(
        val replyId: String,
    ) : ReplyEffect

    data class CancelLike(
        val replyId: String,
    ) : ReplyEffect

    data object ToggleLike : ReplyEffect

    data object ScrollToTop : ReplyEffect

    data class DeleteReply(
        val position: Int,
    ) : ReplyEffect

    data class DeleteSubReply(
        val subReplyId: String,
    ) : ReplyEffect
}
