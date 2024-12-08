package com.teamfilmo.filmo.ui.reply

import com.teamfilmo.filmo.base.effect.BaseEffect

sealed interface ReplyEffect : BaseEffect {
    data object SaveSubReply : ReplyEffect

    data object SaveComplaint : ReplyEffect

    data object SaveBlock : ReplyEffect

    data class SaveLike(
        val replyId: String,
    ) : ReplyEffect

    data class CancelLike(
        val replyId: String,
    ) : ReplyEffect

    data object ScrollToTop : ReplyEffect

    data class DeleteReply(
        val position: Int,
    ) : ReplyEffect
}
