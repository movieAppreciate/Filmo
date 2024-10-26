package com.teamfilmo.filmo.ui.reply

import com.teamfilmo.filmo.base.event.BaseEvent

sealed class ReplyEvent : BaseEvent() {
    data class ClickLike(
        val replyId: String,
    ) : ReplyEvent()

    data class SaveReply(
        val upReplyId: String? = null,
        val reportId: String,
        val content: String,
    ) : ReplyEvent()

    data class DeleteReply(
        val replyId: String,
        val reportId: String,
    ) : ReplyEvent()

    data class DeleteSubReply(
        val replyId: String,
        val reportId: String,
    ) : ReplyEvent()

    data class SaveSubReply(
        val upReplyId: String,
        val reportId: String,
        val content: String,
    ) : ReplyEvent()
}
