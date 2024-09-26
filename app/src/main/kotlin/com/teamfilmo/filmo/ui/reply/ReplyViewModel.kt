package com.teamfilmo.filmo.ui.reply

import androidx.lifecycle.viewModelScope
import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import com.teamfilmo.filmo.data.remote.model.reply.get.GetReplyResponseItem
import com.teamfilmo.filmo.data.remote.model.reply.get.SubReplyResponse
import com.teamfilmo.filmo.data.remote.model.reply.save.SaveReplyRequest
import com.teamfilmo.filmo.data.remote.model.reply.save.SaveReplyResponse
import com.teamfilmo.filmo.domain.reply.GetReplyUseCase
import com.teamfilmo.filmo.domain.reply.SaveReplyUseCase
import com.teamfilmo.filmo.domain.repository.ReplyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class ReplyViewModel
    @Inject
    constructor(
        private val getReplyUseCase: GetReplyUseCase,
        private val saveReplyUseCase: SaveReplyUseCase,
        private val updateReplyUseCase: SaveReplyUseCase,
        private val deleteReplyRepository: ReplyRepository,
    ) : BaseViewModel<ReplyEffect, ReplyEvent>() {
        /*
        답글 리스트
         */
        private val _subReplyListStateFlow =
            MutableStateFlow(
                emptyList<SubReplyResponse>(),
            )

        val subReplyListStateFlow: StateFlow<List<SubReplyResponse>> = _subReplyListStateFlow

        /*
        댓글 아이템
         */
        private val _replyItemStateFlow =
            MutableStateFlow(
                SaveReplyResponse(
                    replyId = "",
                    reportId = "",
                    content = "",
                    userId = "",
                ),
            )

        val replyItemStateFlow: StateFlow<SaveReplyResponse> = _replyItemStateFlow

        /*
        댓글 전체 리스트
         */
        private val _replyListStateFlow = MutableStateFlow(emptyList<GetReplyResponseItem>())
        val replyListStateFlow: StateFlow<List<GetReplyResponseItem>> = _replyListStateFlow

        override fun handleEvent(event: ReplyEvent) {
            when (event) {
                is ReplyEvent.SaveReply -> {
                    saveReply(event.upReplyId, event.reportId, event.content)
                }
            }
        }

        private fun saveReply(
            upReplyId: String? = null,
            reportId: String,
            content: String,
        ) {
            viewModelScope.launch {
                saveReplyUseCase(
                    SaveReplyRequest(
                        upReplyId,
                        reportId,
                        content,
                    ),
                ).collect {
                    Timber.d("댓글 등록 : ${it?.content}")
                    if (it != null) {
                        _replyItemStateFlow.value = it
                    }
                    sendEffect(ReplyEffect.SaveReply)
                }
            }
        }

        fun getReply(reportId: String) {
            viewModelScope.launch {
                getReplyUseCase(reportId).collect {
                    if (it != null) {
                        _replyListStateFlow.value = it
                        Timber.d("_reply : ${replyListStateFlow.value}")
                    }
                }
            }
        }
    }
