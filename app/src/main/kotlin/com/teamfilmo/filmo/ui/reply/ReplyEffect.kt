package com.teamfilmo.filmo.ui.reply

import com.teamfilmo.filmo.base.effect.BaseEffect

sealed interface ReplyEffect : BaseEffect {
    data class DeleteSubReply(
        val upReplyId: String,
    ) : ReplyEffect

    data object SaveSubReply : ReplyEffect

    data class SaveLikeSubReply(
        val upReplyId: String,
        val subReplyId: String,
        val isLiked: Boolean,
    ) : ReplyEffect

    data class CancelLikeSubReply(
        val upReplyId: String,
        val subReplyId: String,
        val isLiked: Boolean,
    ) : ReplyEffect

    data object SaveComplaint : ReplyEffect

    data object SaveBlock : ReplyEffect

    data class SaveLike(
        val replyId: String,
    ) : ReplyEffect

    data class CancelLike(
        val replyId: String,
    ) : ReplyEffect

    data class DeleteReply(
        val replyId: String,
    ) : ReplyEffect
}
