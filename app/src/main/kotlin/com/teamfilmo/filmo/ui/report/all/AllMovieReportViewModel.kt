package com.teamfilmo.filmo.ui.report.all

import androidx.lifecycle.viewModelScope
import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import com.teamfilmo.filmo.domain.bookmark.GetBookmarkLIstUseCase
import com.teamfilmo.filmo.domain.like.CheckLikeStateUseCase
import com.teamfilmo.filmo.domain.like.RegistLikeUseCase
import com.teamfilmo.filmo.domain.report.GetReportListUseCase
import com.teamfilmo.filmo.model.report.ReportItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class AllMovieReportViewModel
    @Inject
    constructor(
        private val getReportListUseCase: GetReportListUseCase,
        private val getBookmarkListUseCase: GetBookmarkLIstUseCase,
        private val checkLikeStateUseCase: CheckLikeStateUseCase,
        private val registLikeUseCase: RegistLikeUseCase,
    ) : BaseViewModel<AllMovieReportEffect, AllMovieReportEvent>() {
        override fun handleEvent(event: AllMovieReportEvent) {
            when (event) {
                is AllMovieReportEvent.RegistLike -> registLike(event.reportId)
                is AllMovieReportEvent.CancelLike -> {}
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

        fun checkLike(reportId: String) {
            viewModelScope.launch {
                checkLikeStateUseCase(reportId)
            }
        }

        fun registLike(reportId: String) {
            viewModelScope.launch {
                registLikeUseCase(reportId)
            }
        }
    }
