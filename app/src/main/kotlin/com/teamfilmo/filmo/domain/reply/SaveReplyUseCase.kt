package com.teamfilmo.filmo.domain.reply

import com.teamfilmo.filmo.data.remote.model.reply.save.SaveReplyRequest
import com.teamfilmo.filmo.data.remote.model.reply.save.SaveReplyResponse
import com.teamfilmo.filmo.domain.repository.ReplyRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class SaveReplyUseCase
    @Inject
    constructor(
        private val replyRepository: ReplyRepository,
    ) {
        operator fun invoke(reply: SaveReplyRequest): Flow<SaveReplyResponse?> =
            flow {
                val result =
                    replyRepository.saveReply(reply)
                        .onFailure {
                            throw it
                        }
                emit(result.getOrNull())
            }
                .catch {
                    Timber.e("failed to save reply : ${it.message}")
                }
    }
