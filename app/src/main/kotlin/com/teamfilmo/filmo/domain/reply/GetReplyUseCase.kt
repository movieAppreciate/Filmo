package com.teamfilmo.filmo.domain.reply

import com.teamfilmo.filmo.data.remote.model.reply.get.GetReplyResponseItem
import com.teamfilmo.filmo.domain.repository.ReplyRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class GetReplyUseCase
    @Inject
    constructor(
        private val replyRepository: ReplyRepository,
    ) {
        operator fun invoke(reportId: String): Flow<List<GetReplyResponseItem>?> =
            flow {
                val result =
                    replyRepository.getReply(reportId = reportId)
                        .onFailure {
                            throw it
                        }
                emit(result.getOrNull())
            }
                .catch {
                    Timber.e("failed to get reply : ${it.message}")
                }
    }
