package com.teamfilmo.filmo.ui.reply

import androidx.lifecycle.viewModelScope
import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import com.teamfilmo.filmo.data.remote.model.reply.get.GetReplyResponseItemWithRole
import com.teamfilmo.filmo.data.remote.model.reply.save.SaveReplyRequest
import com.teamfilmo.filmo.data.remote.model.reply.save.SaveReplyResponse
import com.teamfilmo.filmo.data.remote.model.user.UserResponse
import com.teamfilmo.filmo.domain.reply.DeleteReplyUseCase
import com.teamfilmo.filmo.domain.reply.GetReplyUseCase
import com.teamfilmo.filmo.domain.reply.SaveReplyUseCase
import com.teamfilmo.filmo.domain.user.GetUserInfoUseCase
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
        private val getUserInfoUseCase: GetUserInfoUseCase,
        private val getReplyUseCase: GetReplyUseCase,
        private val saveReplyUseCase: SaveReplyUseCase,
        private val updateReplyUseCase: SaveReplyUseCase,
        private val deleteReplyUseCase: DeleteReplyUseCase,
    ) : BaseViewModel<ReplyEffect, ReplyEvent>() {
        init {
            // 현재 유저 정보
            viewModelScope.launch {
                getUserInfoUseCase().collect {
                    if (it != null) {
                        _userInfo.value = it
                    }
                }
            }
        }

    /*
     본인의 게시글인기?
     */
        private val _isMyReply = MutableStateFlow(false)
        val isMyReply: StateFlow<Boolean> = _isMyReply

    /*
    유저 정보
     */
        private val _userInfo = MutableStateFlow(UserResponse("", "", "", "", "", "", "", "", ""))
        val userInfo: StateFlow<UserResponse> = _userInfo

        /*
        답글 아이템
         */
        private val _subReplyStateFlow = MutableStateFlow(SaveReplyResponse("", "", "", "", ""))
        val subReplyStateFlow: StateFlow<SaveReplyResponse> = _subReplyStateFlow

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
        private val _replyListStateFlow = MutableStateFlow(emptyList<GetReplyResponseItemWithRole>())
        val replyListStateFlow: StateFlow<List<GetReplyResponseItemWithRole>> = _replyListStateFlow

        override fun handleEvent(event: ReplyEvent) {
            when (event) {
                is ReplyEvent.SaveReply -> {
                    saveReply(event.upReplyId, event.reportId, event.content)
                }
                is ReplyEvent.DeleteReply -> {
                    deleteReply(event.replyId, event.reportId)
                }
                is ReplyEvent.DeleteSubReply -> {
                    deleteSubReply(event.replyId, event.reportId)
                }
                is ReplyEvent.SaveSubReply -> {
                    saveSubReply(event.upReplyId, event.reportId, event.content)
                }
            }
        }

        private fun saveSubReply(
            upReplyId: String,
            reportId: String,
            content: String,
        ) {
            viewModelScope.launch {
                saveReplyUseCase(
                    SaveReplyRequest(
                        upReplyId = upReplyId,
                        reportId = reportId,
                        content = content,
                    ),
                ).collect {
                    if (it != null) {
                        _subReplyStateFlow.value = it
                        getReply(reportId)
                    }
                }
            }
        }

        private fun deleteReply(
            reportId: String,
            replyId: String,
        ) {
            viewModelScope.launch {
                deleteReplyUseCase(replyId).collect {
                    val position =
                        _replyListStateFlow.value.indexOfFirst {
                            it.replyId == replyId
                        }
                    sendEffect(ReplyEffect.DeleteReply(position))
                    // getReply(reportId)
                }
            }
        }

        private fun deleteSubReply(
            replyId: String,
            reportId: String,
        ) {
            viewModelScope.launch {
                deleteReplyUseCase(replyId).collect {
                    Timber.d("success to delete sub reply")
                }
                sendEffect(ReplyEffect.DeleteSubReply(replyId))
                getReply(reportId)
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
                        getReply(reportId)
                        sendEffect(ReplyEffect.ScrollToTop)
                    }
                }
            }
        }

        fun getReply(reportId: String) {
            viewModelScope.launch {
                getReplyUseCase(reportId).collect { replyList ->
                    if (replyList != null) {
                        // map 함수의 역할
                        val replyListWithRole =
                            replyList.map {
                                GetReplyResponseItemWithRole(
                                    replyId = it.replyId,
                                    reportId = it.reportId,
                                    content = it.content,
                                    createDate = it.createDate,
                                    lastModifiedDate = it.lastModifiedDate,
                                    nickname = it.nickname,
                                    userId = it.userId,
                                    isMyReply = it.userId == _userInfo.value.userId,
                                    subReply = it.subReply,
                                )
                            }
                        _replyListStateFlow.value = replyListWithRole
                    }
                }
            }
        }
    }
