package com.teamfilmo.filmo.domain.reply

import com.teamfilmo.filmo.data.remote.entity.reply.save.SaveReplyRequest
import com.teamfilmo.filmo.data.remote.entity.reply.save.SaveReplyResponse
import com.teamfilmo.filmo.domain.repository.ReplyRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import timber.log.Timber

class SaveReplyUseCase
    @Inject
    constructor(
        private val replyRepository: ReplyRepository,
    ) {
        operator fun invoke(reply: SaveReplyRequest): Flow<SaveReplyResponse?> =
            flow {
                replyRepository
                    .saveReply(reply)
                    .onSuccess {
                        emit(it)
                    }.onFailure {
                        when (it) {
                            is HttpException -> Timber.e("Network error: ${it.message}")
                            else -> Timber.e("Unknown error: ${it.message}")
                        }
                        emit(null)
                    }
            }
    }
