package com.teamfilmo.filmo.ui.reply

import androidx.lifecycle.viewModelScope
import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import com.teamfilmo.filmo.data.remote.model.block.SaveBlockRequest
import com.teamfilmo.filmo.data.remote.model.complaint.SaveComplaintRequest
import com.teamfilmo.filmo.data.remote.model.like.CheckLikeResponse
import com.teamfilmo.filmo.data.remote.model.like.CountLikeResponse
import com.teamfilmo.filmo.data.remote.model.like.SaveLikeRequest
import com.teamfilmo.filmo.data.remote.model.like.SaveLikeResponse
import com.teamfilmo.filmo.data.remote.model.reply.get.GetReplyResponseItemWithRole
import com.teamfilmo.filmo.data.remote.model.reply.save.SaveReplyRequest
import com.teamfilmo.filmo.data.remote.model.reply.save.SaveReplyResponse
import com.teamfilmo.filmo.data.remote.model.user.UserInfo
import com.teamfilmo.filmo.domain.block.SaveBlockUseCase
import com.teamfilmo.filmo.domain.complaint.SaveComplaintUseCase
import com.teamfilmo.filmo.domain.like.CancelLikeUseCase
import com.teamfilmo.filmo.domain.like.CheckLikeStateUseCase
import com.teamfilmo.filmo.domain.like.CountLikeUseCase
import com.teamfilmo.filmo.domain.like.SaveLikeUseCase
import com.teamfilmo.filmo.domain.reply.DeleteReplyUseCase
import com.teamfilmo.filmo.domain.reply.GetReplyUseCase
import com.teamfilmo.filmo.domain.reply.SaveReplyUseCase
import com.teamfilmo.filmo.domain.repository.UserPreferencesRepository
import com.teamfilmo.filmo.domain.user.GetUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class ReplyViewModel
    @Inject
    constructor(
        private val getUserInfoUseCase: GetUserInfoUseCase,
        private val userPreferencesRepository: UserPreferencesRepository,
        private val saveBlockUseCase: SaveBlockUseCase,
        private val saveComplaintUseCase: SaveComplaintUseCase,
        private val countLikeUseCase: CountLikeUseCase,
        private val checkLikeUseCase: CheckLikeStateUseCase,
        private val saveLikeUseCase: SaveLikeUseCase,
        private val cancelLikeUseCase: CancelLikeUseCase,
        private val getReplyUseCase: GetReplyUseCase,
        private val saveReplyUseCase: SaveReplyUseCase,
        private val deleteReplyUseCase: DeleteReplyUseCase,
    ) : BaseViewModel<ReplyEffect, ReplyEvent>() {
        init {
            // 현재 유저 정보를 Db에서 가져오기
            viewModelScope.launch {
                getUserInfoUseCase().collect {
                    if (it != null) {
                        _userInfo.value =
                            UserInfo(
                                type = it.type,
                                userId = it.userId,
                                nickName = it.nickname,
                                roles = it.roles,
                            )
                    }
                }
            }
        }

        // 좋아요 여부
        private val _checkLikeResponse = MutableStateFlow(CheckLikeResponse())
        val checkLikeResponse = _checkLikeResponse.asStateFlow()

    /*
    좋아요 상태
     */
        private val _isLiked = MutableStateFlow(false)
        val isLiked: StateFlow<Boolean> = _isLiked

    /*
    좋아요 저장
     */

        private val _saveReplyLikeResponse = MutableStateFlow(SaveLikeResponse())
        val saveReplyLikeResponse: StateFlow<SaveLikeResponse> = _saveReplyLikeResponse

    /*
    좋아요 수

     */
        private val _likeCount = MutableStateFlow(0)
        val likeCount: StateFlow<Int> = _likeCount

    /*
     본인의 게시글인기?
     */
        private val _isMyReply = MutableStateFlow(false)
        val isMyReply: StateFlow<Boolean> = _isMyReply

    /*
    유저 정보
     */
        private val _userInfo = MutableStateFlow(UserInfo())
        val userInfo: StateFlow<UserInfo> = _userInfo

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
                is ReplyEvent.SaveBlock -> {
                    saveReplyBlock(event.targetId)
                }
                is ReplyEvent.SaveComplaint -> {
                    saveReplyComplaint(event.targetId)
                }
                is ReplyEvent.ClickLike -> {
                    toggleLike(event.replyId)
                }
                is ReplyEvent.SaveReply -> {
                    saveReply(event.upReplyId, event.reportId, event.content)
                }
                is ReplyEvent.DeleteReply -> {
                    deleteReply(event.replyId)
                }
                is ReplyEvent.DeleteSubReply -> {
                    deleteSubReply(event.replyId, event.reportId)
                }
                is ReplyEvent.SaveSubReply -> {
                    saveSubReply(event.upReplyId, event.reportId, event.content)
                }
            }
        }

    /*
    댓글 차단
     */
        private fun saveReplyBlock(targetId: String) {
            viewModelScope.launch {
                saveBlockUseCase(SaveBlockRequest(targetId)).collect {
                    // _saveReplyBlockResponse.value = it
                    sendEffect(ReplyEffect.SaveBlock)
                }
            }
        }

    /*
     댓글 신고
     */
        private fun saveReplyComplaint(targetId: String) {
            viewModelScope.launch {
                saveComplaintUseCase(SaveComplaintRequest(targetId, "reply")).collect {
                    if (it != null) {
                        sendEffect(ReplyEffect.SaveComplaint)
                    }
                }
            }
        }
    /*
    댓글 좋아요
     */

        private fun saveReplyLike(
            targetId: String,
            type: String = "reply",
        ) {
            viewModelScope.launch {
                saveLikeUseCase(SaveLikeRequest(targetId, type)).collect {
                    if (it != null) {
                        _saveReplyLikeResponse.value = it
                        _isLiked.value = true
                    }
                }
            }
        }

        private fun cancelReplyLike(
            likeId: String,
        ) {
            viewModelScope.launch {
                cancelLikeUseCase(likeId).collect {
                    if (it != null) {
                        _isLiked.value = false
                    }
                }
            }
        }

        private fun checkLikeState(replyId: String) {
            viewModelScope.launch {
                checkLikeUseCase(targetId = replyId).collect {
                    if (it != null) _checkLikeResponse.value = it
                }
            }
        }

        // 좋아요 토글 , 좋아요 여부가 true인 경우 취소, 좋아요 여부가 False인 경우 좋아요 등록
        private fun toggleLike(
            targetId: String,
            type: String = "reply",
        ) {
            viewModelScope.launch {
                combine(
                    checkLikeUseCase(targetId, type),
                    countLikeUseCase(targetId),
                ) { isLiked, likeCount ->
                    Pair(isLiked, likeCount)
                }.collect { (checkLike, likeCount) ->
                    if (checkLike != null && likeCount != null) {
                        if (checkLike.isLike) {
                            // todo : likeId 수정 필요
                            cancelReplyLike(likeId = checkLike.likeId!!)
                            // 여기서 isLiked를 그대로 넣어줘서 정상적으로 작동하지 않은 거였다!!!
                            updateLikeState(targetId, false, likeCount.countLike - 1)
                            sendEffect(ReplyEffect.CancelLike(replyId = targetId))
                        } else {
                            saveReplyLike(targetId, type)
                            updateLikeState(targetId, true, likeCount.countLike + 1)
                            sendEffect(ReplyEffect.SaveLike(targetId))
                        }
                    }
                }
            }
        }

        private fun updateLikeState(
            targetId: String,
            isLiked: Boolean,
            likeCount: Int,
        ) {
            viewModelScope.launch {
                val currentList = _replyListStateFlow.value.toMutableList()
                val position = currentList.indexOfFirst { it.replyId == targetId }
                val updatedReply =
                    currentList[position].copy(isLiked = isLiked, likeCount = likeCount)
                currentList[position] = updatedReply
                _replyListStateFlow.value = currentList
            }
        }

        private fun updateLikeCountState(
            targetId: String,
            likeCount: Int,
        ) {
            viewModelScope.launch {
                val currentList = _replyListStateFlow.value.toMutableList()
                val position = currentList.indexOfFirst { it.replyId == targetId }
                val updatedReply =
                    currentList[position].copy(likeCount = likeCount)
                currentList[position] = updatedReply
                _replyListStateFlow.value = currentList
                // sendEffect(ReplyEffect.ToggleLike)
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
            replyId: String,
        ) {
            viewModelScope.launch {
                deleteReplyUseCase(replyId).collect {
                    val position =
                        _replyListStateFlow.value.indexOfFirst {
                            it.replyId == replyId
                        }
                    sendEffect(ReplyEffect.DeleteReply(position))
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
//                sendEffect(ReplyEffect.DeleteSubReply(replyId))
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
                    if (it != null) {
                        _replyItemStateFlow.value = it
                        sendEffect(ReplyEffect.ScrollToTop)
                        getReply(reportId)
                    }
                }
            }
        }

        fun getReply(reportId: String) {
            viewModelScope.launch {
                getReplyUseCase(reportId).collect { replyList ->
                    if (replyList != null) {
                        val likeStateFlows =
                            replyList.map {
                                checkLikeUseCase(it.replyId)
                            }
                        val countStateFlows =
                            replyList.map {
                                countLikeUseCase(it.replyId)
                            }
                        combine(likeStateFlows + countStateFlows) { combinedStates ->
                            // combinedStates 배열에서 좋아요 상태와 좋아요 수를 분리
                            val likeStates = combinedStates.take(replyList.size)
                            val likeCounts = combinedStates.drop(replyList.size)

                            // map 함수의 역할
                            replyList.mapIndexed { index, reply ->
                                GetReplyResponseItemWithRole(
                                    replyId = reply.replyId,
                                    reportId = reply.reportId,
                                    content = reply.content,
                                    createDate = reply.createDate,
                                    lastModifiedDate = reply.lastModifiedDate,
                                    nickname = reply.nickname,
                                    userId = reply.userId,
                                    isMyReply = reply.userId == _userInfo.value.userId,
                                    subReply = reply.subReply,
                                    isLiked = (likeStates[index] as CheckLikeResponse).isLike,
                                    likeCount = (likeCounts[index] as CountLikeResponse).countLike,
                                )
                            }
                        }.collect {
                            _replyListStateFlow.value = it
                            Timber.d("리스트 업뎃 :$it")
                            sendEffect(ReplyEffect.ScrollToTop)
                        }
                    }
                }
            }
        }
    }
