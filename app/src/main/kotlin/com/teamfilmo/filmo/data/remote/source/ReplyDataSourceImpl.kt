package com.teamfilmo.filmo.data.remote.source

import com.teamfilmo.filmo.data.remote.entity.reply.DeleteReplyResponse
import com.teamfilmo.filmo.data.remote.entity.reply.get.GetReplyResponseItem
import com.teamfilmo.filmo.data.remote.entity.reply.save.SaveReplyRequest
import com.teamfilmo.filmo.data.remote.entity.reply.save.SaveReplyResponse
import com.teamfilmo.filmo.data.remote.entity.reply.update.UpdateReplyRequest
import com.teamfilmo.filmo.data.remote.entity.reply.update.UpdateReplyResponse
import com.teamfilmo.filmo.data.remote.service.ReplyService
import com.teamfilmo.filmo.data.source.ReplyDataSource
import javax.inject.Inject

class ReplyDataSourceImpl
    @Inject
    constructor(
        private val replyService: ReplyService,
    ) : ReplyDataSource {
        override suspend fun saveReply(request: SaveReplyRequest): Result<SaveReplyResponse> = replyService.saveReply(request)

        override suspend fun updateReply(request: UpdateReplyRequest): Result<UpdateReplyResponse> = replyService.updateReply(request)

        override suspend fun getReply(reportId: String): Result<List<GetReplyResponseItem>> = replyService.getReply(reportId)

        override suspend fun deleteReply(replyId: String): Result<DeleteReplyResponse> = replyService.deleteReply(replyId)
    }
