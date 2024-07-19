package com.teamfilmo.filmo.ui.report.all

import androidx.lifecycle.viewModelScope
import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import com.teamfilmo.filmo.domain.bookmark.GetBookmarkLIstUseCase
import com.teamfilmo.filmo.domain.like.CancelLikeUseCase
import com.teamfilmo.filmo.domain.like.CheckLikeStateUseCase
import com.teamfilmo.filmo.domain.like.RegistLikeUseCase
import com.teamfilmo.filmo.domain.report.GetReportListUseCase
import com.teamfilmo.filmo.model.report.ReportItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber


@HiltViewModel
class AllMovieReportViewModel
    @Inject
    constructor(
        private val getReportListUseCase: GetReportListUseCase,
        private val getBookmarkListUseCase: GetBookmarkLIstUseCase,
        private val checkLikeStateUseCase: CheckLikeStateUseCase,
        private val registerLikeUseCase: RegistLikeUseCase,
        private val cancelLikeUseCase: CancelLikeUseCase,
    ) : BaseViewModel<AllMovieReportEffect, AllMovieReportEvent>() {

        override fun handleEvent(event: AllMovieReportEvent) {
            when (event) {
                is AllMovieReportEvent.RegistLike -> registLike(event.reportId)
                is AllMovieReportEvent.CancelLike -> {
                    cancelLike(event.reportId)
                }
            }
        }

        private val _list =
            combine(
                getReportListUseCase(),
                getBookmarkListUseCase(),
            ) {
                    reportList, bookmarkList ->
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
            }

        val list: StateFlow<List<ReportItem>> =
            _list.stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = emptyList(),
            )

        private fun registLike(reportId: String) {
            Timber.d("좋아요 등록")
            viewModelScope.launch {
                registerLikeUseCase(reportId)
            }
            sendEffect(AllMovieReportEffect.RegistLike(reportId))
        }

        private fun cancelLike(reportId: String) {
            Timber.d("좋아요 취소")
            viewModelScope.launch {
                cancelLikeUseCase(reportId)
            }
            sendEffect(AllMovieReportEffect.CancelLike(reportId))
        }

        fun checkLike(reportId: String): Flow<Boolean> {
            val result = checkLikeStateUseCase(reportId)
            result.stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = false,
            )
            return result
        }
    }
