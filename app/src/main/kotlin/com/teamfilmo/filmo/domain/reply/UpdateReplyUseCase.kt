package com.teamfilmo.filmo.domain.reply

import com.teamfilmo.filmo.data.remote.model.reply.update.UpdateReplyRequest
import com.teamfilmo.filmo.data.remote.model.reply.update.UpdateReplyResponse
import com.teamfilmo.filmo.domain.repository.ReplyRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class UpdateReplyUseCase
    @Inject
    constructor(
        private val replyRepository: ReplyRepository,
    ) {
        operator fun invoke(request: UpdateReplyRequest): Flow<UpdateReplyResponse?> =
            flow {
                val result =
                    replyRepository.updateReply(request)
                        .onFailure {
                            throw it
                        }
                emit(result.getOrNull())
            }.catch {
                Timber.d("failed to update reply : ${it.message}")
            }
    }
