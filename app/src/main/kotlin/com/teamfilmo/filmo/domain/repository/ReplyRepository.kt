package com.teamfilmo.filmo.domain.repository

import com.teamfilmo.filmo.data.remote.entity.reply.DeleteReplyResponse
import com.teamfilmo.filmo.data.remote.entity.reply.get.GetReplyResponseItem
import com.teamfilmo.filmo.data.remote.entity.reply.save.SaveReplyRequest
import com.teamfilmo.filmo.data.remote.entity.reply.save.SaveReplyResponse
import com.teamfilmo.filmo.data.remote.entity.reply.update.UpdateReplyRequest
import com.teamfilmo.filmo.data.remote.entity.reply.update.UpdateReplyResponse

interface ReplyRepository {
    suspend fun saveReply(request: SaveReplyRequest): Result<SaveReplyResponse>

    suspend fun updateReply(request: UpdateReplyRequest): Result<UpdateReplyResponse>

    suspend fun getReply(reportId: String): Result<List<GetReplyResponseItem>>

    suspend fun deleteReply(replyId: String): Result<DeleteReplyResponse>
}
