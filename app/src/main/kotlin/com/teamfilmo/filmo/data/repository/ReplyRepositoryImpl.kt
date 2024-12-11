package com.teamfilmo.filmo.data.repository

import com.teamfilmo.filmo.data.remote.model.reply.DeleteReplyResponse
import com.teamfilmo.filmo.data.remote.model.reply.get.GetReplyResponseItem
import com.teamfilmo.filmo.data.remote.model.reply.save.SaveReplyRequest
import com.teamfilmo.filmo.data.remote.model.reply.save.SaveReplyResponse
import com.teamfilmo.filmo.data.remote.model.reply.update.UpdateReplyRequest
import com.teamfilmo.filmo.data.remote.model.reply.update.UpdateReplyResponse
import com.teamfilmo.filmo.data.source.ReplyDataSource
import com.teamfilmo.filmo.domain.repository.ReplyRepository
import javax.inject.Inject

class ReplyRepositoryImpl
    @Inject
    constructor(
        private val replyDataSource: ReplyDataSource,
    ) : ReplyRepository {
        override suspend fun saveReply(request: SaveReplyRequest): Result<SaveReplyResponse> = replyDataSource.saveReply(request)

        override suspend fun updateReply(request: UpdateReplyRequest): Result<UpdateReplyResponse> = replyDataSource.updateReply(request)

        override suspend fun getReply(reportId: String): Result<List<GetReplyResponseItem>> = replyDataSource.getReply(reportId)

        override suspend fun deleteReply(replyId: String): Result<DeleteReplyResponse> = replyDataSource.deleteReply(replyId)
    }
