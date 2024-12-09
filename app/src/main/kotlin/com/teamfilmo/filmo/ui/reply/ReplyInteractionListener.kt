package com.teamfilmo.filmo.ui.reply

interface ReplyInteractionListener {
    // 부모 댓글 관련 이벤트
    fun onReplyClick(position: Int)

    fun onReplyLikeClick(replyId: String)

    fun onReplyMoreClick(
        isMyReply: Boolean,
        replyId: String,
    )

    fun onShowBottomSheet(
        replyId: String,
        position: Int,
    )

    fun onSubReplyLikeClick(
        parentReplyId: String,
        subReplyId: String,
    )

    fun onSubReplyMoreClick(
        isMyReply: Boolean,
        parentReplyId: String,
        subReplyId: String,
    )
}

interface SubReplyInteractionListener {
    // 자식 답글 관련 이벤트

    fun onSubReplyLikeClick(
        parentReplyId: String,
        subReplyId: String,
    )

    fun onSubReplyMoreClick(
        isMyReply: Boolean,
        parentReplyId: String,
        subReplyId: String,
    )

    fun onShowBottomSheet(
        replyId: String,
        position: Int,
    )
}
