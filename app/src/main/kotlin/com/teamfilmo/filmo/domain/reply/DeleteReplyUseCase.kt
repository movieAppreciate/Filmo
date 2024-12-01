package com.teamfilmo.filmo.domain.reply

import com.teamfilmo.filmo.domain.repository.ReplyRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import timber.log.Timber

class DeleteReplyUseCase
    @Inject
    constructor(
        private val replyRepository: ReplyRepository,
    ) {
        operator fun invoke(replyId: String): Flow<String?> =
            flow {
                replyRepository
                    .deleteReply(replyId)
                    .onFailure {
                        when (it) {
                            is HttpException -> Timber.e("Network error: ${it.message}")
                            else -> Timber.e("Unknown error: ${it.message}")
                        }
                        emit(null)
                    }.onSuccess {
                        emit(it)
                    }
            }
    }
