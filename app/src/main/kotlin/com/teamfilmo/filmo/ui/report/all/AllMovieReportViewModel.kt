package com.teamfilmo.filmo.ui.report.all

import androidx.lifecycle.viewModelScope
import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import com.teamfilmo.filmo.domain.bookmark.GetBookmarkLIstUseCase
import com.teamfilmo.filmo.domain.like.GetLikeStateUseCase
import com.teamfilmo.filmo.domain.report.GetReportListUseCase
import com.teamfilmo.filmo.model.report.ReportItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class AllMovieReportViewModel
    @Inject
    constructor(
        private val getReportListUseCase: GetReportListUseCase,
        private val getBookmarkListUseCase: GetBookmarkLIstUseCase,
    ) : BaseViewModel<AllMovieReportEffect, AllMovieReportEvent>() {
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
                        bookmarkList.filter {
                            it.reportId == reportItem.reportId
                        },
                    )
                }
            }

        val list: StateFlow<List<ReportItem>> =
            _list.stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = emptyList(),
            )
    }
