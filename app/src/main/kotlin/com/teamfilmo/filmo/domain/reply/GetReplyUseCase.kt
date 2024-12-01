package com.teamfilmo.filmo.domain.reply

import com.teamfilmo.filmo.data.remote.model.reply.get.GetReplyResponseItem
import com.teamfilmo.filmo.domain.repository.ReplyRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import timber.log.Timber

class GetReplyUseCase
    @Inject
    constructor(
        private val replyRepository: ReplyRepository,
    ) {
        operator fun invoke(reportId: String): Flow<List<GetReplyResponseItem>?> =
            flow {
                replyRepository
                    .getReply(reportId = reportId)
                    .onFailure {
                        when (it) {
                            is HttpException -> Timber.e("Network error: ${it.message}")
                            else -> Timber.e("Unknown error: ${it.message}")
                        }
                        emit(emptyList())
                    }.onSuccess {
                        emit(it)
                    }
            }
    }
