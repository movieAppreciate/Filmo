package com.teamfilmo.filmo.domain.reply

import com.teamfilmo.filmo.domain.repository.ReplyRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class DeleteReplyUseCase
    @Inject
    constructor(
        private val replyRepository: ReplyRepository,
    ) {
        operator fun invoke(replyId: String): Flow<String?> =
            flow {
                val result =
                    replyRepository.deleteReply(replyId)
                        .onFailure {
                            throw it
                        }
                emit(result.getOrNull())
            }.catch {
                Timber.e("failed to delete reply : ${it.localizedMessage}")
            }
    }
