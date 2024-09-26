package com.teamfilmo.filmo.ui.reply

import com.teamfilmo.filmo.base.event.BaseEvent

sealed class ReplyEvent : BaseEvent() {
    data class SaveReply(
        val upReplyId: String? = null,
        val reportId: String,
        val content: String,
    ) : ReplyEvent()
}
