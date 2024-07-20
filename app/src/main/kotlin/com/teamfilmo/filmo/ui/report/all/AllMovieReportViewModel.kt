package com.teamfilmo.filmo.ui.report.all

import androidx.lifecycle.viewModelScope
import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import com.teamfilmo.filmo.domain.bookmark.GetBookmarkLIstUseCase
import com.teamfilmo.filmo.domain.bookmark.RegistBookmarkUseCase
import com.teamfilmo.filmo.domain.like.CancelLikeUseCase
import com.teamfilmo.filmo.domain.like.CheckLikeStateUseCase
import com.teamfilmo.filmo.domain.like.RegistLikeUseCase
import com.teamfilmo.filmo.domain.report.GetReportListUseCase
import com.teamfilmo.filmo.model.report.ReportItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

data class AllReportUiState(
    val reportId: String = "",
    var likeCount: Int = 0,
)

@HiltViewModel
class AllMovieReportViewModel
    @Inject
    constructor(
        private val getReportListUseCase: GetReportListUseCase,
        private val getBookmarkListUseCase: GetBookmarkLIstUseCase,
        private val checkLikeStateUseCase: CheckLikeStateUseCase,
        private val registerLikeUseCase: RegistLikeUseCase,
        private val cancelLikeUseCase: CancelLikeUseCase,
        private val registBookmarkUseCase: RegistBookmarkUseCase,
    ) : BaseViewModel<AllMovieReportEffect, AllMovieReportEvent>() {
        private val _uiState = MutableStateFlow<List<AllReportUiState>>(emptyList())
        val uiState: StateFlow<List<AllReportUiState>> = _uiState.asStateFlow()

        override fun handleEvent(event: AllMovieReportEvent) {
            when (event) {
                is AllMovieReportEvent.ClickLike -> toggleLike(event.reportId)
            }
        }

        private val _allMovieReportList =
            combine(
                getReportListUseCase(),
                getBookmarkListUseCase(),
            ) { reportList, bookmarkList ->
                val reports =
                    reportList.map { reportItem ->
                        ReportItem(
                            reportItem.reportId,
                            reportItem.title,
                            reportItem.content,
                            reportItem.createDate,
                            reportItem.imageUrl,
                            reportItem.nickname,
                            reportItem.likeCount,
                            reportItem.replyCount,
                            reportItem.bookmarkCount,
                            bookmarkList.any {
                                it.reportId == reportItem.reportId
                            },
                            checkLikeStateUseCase(reportItem.reportId).first(),
                        )
                    }
                _uiState.value =
                    reports.map { reportItem ->
                        AllReportUiState(
                            reportId = reportItem.reportId,
                            likeCount = reportItem.likeCount,
                        )
                    }
                reports
            }

        val allMovieReportList: StateFlow<List<ReportItem>> =
            _allMovieReportList.stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = emptyList(),
            )

    /* 좋아요 등록
     */

        private fun registLike(reportId: String) {
            Timber.d("좋아요 등록")
            viewModelScope.launch {
                registerLikeUseCase(reportId)
                sendEffect(AllMovieReportEffect.RegistLike(reportId))
                updateLikeCount(reportId, true)
            }
        }

/*
좋아요 취소
 */
        private fun cancelLike(reportId: String) {
            Timber.d("좋아요 취소")
            viewModelScope.launch {
                cancelLikeUseCase(reportId)
                sendEffect(AllMovieReportEffect.CancelLike(reportId))
                updateLikeCount(reportId, false)
            }
        }

/*
좋아요 토글
 */
        private fun toggleLike(reportId: String) {
            viewModelScope.launch {
                checkLikeStateUseCase(reportId).collect {
                    if (it) {
                        cancelLike(reportId)
                    } else {
                        registLike(reportId)
                    }
                }
            }
        }
        // 좋아요 수 업데이트

        private fun updateLikeCount(
            reportId: String,
            isLiked: Boolean,
        ) {
            var updatedLikeCount = 0
            _uiState.update { uiStateList ->
                uiStateList.map { uiState ->
                    if (uiState.reportId == reportId) {
                        updatedLikeCount = if (isLiked) uiState.likeCount + 1 else uiState.likeCount - 1
                        uiState.copy(likeCount = updatedLikeCount)
                    } else {
                        uiState
                    }
                }
            }
            sendEffect(AllMovieReportEffect.CountLike(reportId, updatedLikeCount))
        }

    /*
    북마크
     */
        fun registerBookmark(reportId: String) {
            viewModelScope.launch {
                registBookmarkUseCase(reportId)
            }
        }
    }
