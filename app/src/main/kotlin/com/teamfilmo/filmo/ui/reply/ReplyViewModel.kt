package com.teamfilmo.filmo.ui.reply

import androidx.lifecycle.viewModelScope
import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import com.teamfilmo.filmo.data.remote.entity.block.SaveBlockRequest
import com.teamfilmo.filmo.data.remote.entity.complaint.SaveComplaintRequest
import com.teamfilmo.filmo.data.remote.entity.like.CheckLikeResponse
import com.teamfilmo.filmo.data.remote.entity.like.CountLikeResponse
import com.teamfilmo.filmo.data.remote.entity.like.SaveLikeRequest
import com.teamfilmo.filmo.data.remote.entity.like.SaveLikeResponse
import com.teamfilmo.filmo.data.remote.entity.reply.save.SaveReplyRequest
import com.teamfilmo.filmo.domain.block.SaveBlockUseCase
import com.teamfilmo.filmo.domain.complaint.SaveComplaintUseCase
import com.teamfilmo.filmo.domain.like.CancelLikeUseCase
import com.teamfilmo.filmo.domain.like.CheckLikeStateUseCase
import com.teamfilmo.filmo.domain.like.CountLikeUseCase
import com.teamfilmo.filmo.domain.like.SaveLikeUseCase
import com.teamfilmo.filmo.domain.model.reply.GetReplyItemWithRole
import com.teamfilmo.filmo.domain.model.user.UserInfo
import com.teamfilmo.filmo.domain.reply.DeleteReplyUseCase
import com.teamfilmo.filmo.domain.reply.GetReplyUseCase
import com.teamfilmo.filmo.domain.reply.SaveReplyUseCase
import com.teamfilmo.filmo.domain.user.GetUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber

data class ReplyLikeState(
    val likeId: String? = null,
    val likeCount: Int = 0,
    val isLiked: Boolean = false,
)

data class SubReplyLikeState(
    val subReplyId: String? = null,
    val likeId: String? = null,
    val likeCount: Int = 0,
    val isLiked: Boolean = false,
)

@HiltViewModel
class ReplyViewModel
    @Inject
    constructor(
        private val getUserInfoUseCase: GetUserInfoUseCase,
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
        val list = mutableListOf<SubReplyWithLikeInfo>()

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

        //  답글 좋아요 UiState
        private val _subReplyLikeUiState = MutableStateFlow(SubReplyLikeState())
        val subReplyLikeUiState = _subReplyLikeUiState.asStateFlow()

        //  답글 좋아요
        private val _saveSubReplyResponse = MutableStateFlow(SaveLikeResponse())
        val saveSubReplyResponse = _saveSubReplyResponse.asStateFlow()

        // 댓글 좋아요 UI State
        private val _replyLikeState = MutableStateFlow(ReplyLikeState())
        val replyLikeState = _replyLikeState.asStateFlow()

        // 좋아요 정보
        private val _saveReplyLikeResponse = MutableStateFlow(SaveLikeResponse())
        val saveReplyLikeResponse = _saveReplyLikeResponse.asStateFlow()

        // 답글의 좋아요 여부
        private val _subReplyLikeState = MutableStateFlow(CheckLikeResponse())
        val subReplyLikeState = _subReplyLikeState.asStateFlow()

        // 답글의 좋아요 수
        private val _subReplyLikeCount = MutableStateFlow(0)
        val subReplyLikeCount = _subReplyLikeCount.asStateFlow()

        // 좋아요 수
        private val _likeCount = MutableStateFlow(0)
        val likeCount: StateFlow<Int> = _likeCount

        // 유저 정보
        private val _userInfo = MutableStateFlow(UserInfo())
        val userInfo: StateFlow<UserInfo> = _userInfo

        // 댓글 전체 리스트
        private val _replyListStateFlow = MutableStateFlow(emptyList<GetReplyItemWithRole>())
        val replyListStateFlow: StateFlow<List<GetReplyItemWithRole>> = _replyListStateFlow

        // 답글 전체 리스트
        private val _subReplyList = MutableStateFlow(emptyList<SubReplyWithLikeInfo>())
        val subReplyList = _subReplyList.asStateFlow()

        override fun handleEvent(event: ReplyEvent) {
            when (event) {
                is ReplyEvent.ClickSubReplyLike -> {
                    toggleSubReplyLike(event.replyId)
                }
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

        // 댓글 차단
        private fun saveReplyBlock(targetId: String) {
            viewModelScope.launch {
                saveBlockUseCase(SaveBlockRequest(targetId)).collect {
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

        // 댓글 종하요
        private fun saveReplyLike(
            targetId: String,
            type: String = "reply",
        ) {
            viewModelScope.launch {
                saveLikeUseCase(SaveLikeRequest(targetId, type)).collect {
                    if (it != null) {
                        _saveReplyLikeResponse.value = it
                        sendEffect(ReplyEffect.SaveLike(targetId))
                    }
                }
            }
        }

        // 답글 종하요
        private fun saveSubReplyLike(
            targetId: String,
            type: String = "reply",
        ) {
            viewModelScope.launch {
                saveLikeUseCase(
                    SaveLikeRequest(
                        targetId,
                        type,
                    ),
                ).collect {
                    if (it != null) _saveSubReplyResponse.value = it
                    val subReply = _subReplyList.value.find { it.replyId == targetId }
                    if (subReply != null) {
                        sendEffect(
                            ReplyEffect.SaveLikeSubReply(
                                subReply.upReplyId,
                                subReply.replyId,
                                true,
                            ),
                        )
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
                        sendEffect(ReplyEffect.CancelLike(_saveReplyLikeResponse.value.targetId))
                    }
                }
            }
        }

        // 답글 좋아요 취소
        private fun cancelSubReplyLike(
            likeId: String,
            targetId: String,
        ) {
            viewModelScope.launch {
                cancelLikeUseCase(likeId).collect {
                    if (it != null) {
                        val subReply = _subReplyList.value.find { it.replyId == _saveSubReplyResponse.value.targetId }
                        if (subReply != null) {
                            sendEffect(
                                ReplyEffect.CancelLikeSubReply(
                                    subReply.upReplyId,
                                    subReply.replyId,
                                    false,
                                ),
                            )
                        }
                    }
                }
            }
        }

        // 답글 좋아요 토큰
        private fun toggleSubReplyLike(targetId: String) {
            viewModelScope.launch {
                checkLikeUseCase(targetId).collect {
                    if (it != null) {
                        if (it.isLike && it.likeId != null) {
                            cancelSubReplyLike(it.likeId, targetId)
                        } else {
                            saveSubReplyLike(targetId)
                        }
                    }
                }
            }
        }

        // 댓글 좋아요 토글 , 좋아요 여부가 true인 경우 취소, 좋아요 여부가 False인 경우 좋아요 등록
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
                        getReply(reportId)
                        sendEffect(ReplyEffect.SaveSubReply)
                    }
                }
            }
        }

        private fun deleteReply(
            replyId: String,
        ) {
            viewModelScope.launch {
                deleteReplyUseCase(replyId).collect {
                    if (it != null && it.success) {
                        sendEffect(ReplyEffect.DeleteReply(replyId))
                    }
                }
            }
        }

        private fun deleteSubReply(
            replyId: String,
            reportId: String,
        ) {
            viewModelScope.launch {
                deleteReplyUseCase(replyId).collect {
                    if (it != null) {
                        getReply(reportId)
                        sendEffect(ReplyEffect.DeleteSubReply(replyId))
                    }
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
                    if (it != null) {
                        getReply(reportId)
                    }
                }
            }
        }

        private suspend fun getSubReplyLikeInfo(targetId: String): Pair<Int, Boolean> =
            coroutineScope {
                val likeCountDeferred =
                    async {
                        countLikeUseCase(targetId = targetId).first()
                    }

                val likeStateDeferred =
                    async {
                        checkLikeUseCase(targetId).first()
                    }
                Pair(
                    likeCountDeferred.await()?.countLike ?: 0,
                    likeStateDeferred.await()?.isLike ?: false,
                )
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
                            coroutineScope {
                                // combinedStates 배열에서 좋아요 상태와 좋아요 수를 분리
                                val likeStates = combinedStates.take(replyList.size)
                                val likeCounts = combinedStates.drop(replyList.size)

                                // map 함수의 역할
                                replyList.mapIndexed { index, reply ->
                                    val subReplyList =
                                        reply.subReply
                                            ?.map {
                                                Timber.d("최초 답글 :${it.replyId}")
                                                async {
                                                    val (likeCount, isLiked) = getSubReplyLikeInfo(targetId = it.replyId)

                                                    SubReplyWithLikeInfo(
                                                        replyId = it.replyId,
                                                        content = it.content,
                                                        createDate = it.createDate,
                                                        lastModifiedDate = it.lastModifiedDate,
                                                        nickname = it.nickname,
                                                        reportId = it.reportId,
                                                        upReplyId = it.upReplyId,
                                                        userId = it.userId,
                                                        likeCount = likeCount,
                                                        isLiked = isLiked,
                                                        isMySubReply = false,
                                                    )
                                                }
                                            }?.awaitAll()
                                    if (subReplyList != null) {
                                        list.addAll(subReplyList)
                                        _subReplyList.value = list
                                    }
                                    GetReplyItemWithRole(
                                        replyId = reply.replyId,
                                        reportId = reply.reportId,
                                        content = reply.content,
                                        createDate = reply.createDate,
                                        lastModifiedDate = reply.lastModifiedDate,
                                        nickname = reply.nickname,
                                        userId = reply.userId,
                                        isMyReply = reply.userId == _userInfo.value.userId,
                                        // todo : 매핑해주기
                                        subReply = subReplyList,
                                        isLiked = (likeStates[index] as CheckLikeResponse).isLike,
                                        likeCount = (likeCounts[index] as CountLikeResponse).countLike,
                                    )
                                }
                            }
                        }.collect {
                            _replyListStateFlow.value = it.reversed()
                        }
                    }
                }
            }
        }
    }

data class SubReplyWithLikeInfo(
    val content: String,
    val createDate: String,
    val lastModifiedDate: String,
    val nickname: String,
    val replyId: String,
    val reportId: String,
    val upReplyId: String,
    val userId: String,
    val likeCount: Int,
    val isLiked: Boolean,
    val isMySubReply: Boolean,
)
