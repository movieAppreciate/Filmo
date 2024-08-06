package com.teamfilmo.filmo.ui.report.all

import androidx.lifecycle.viewModelScope
import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import com.teamfilmo.filmo.data.remote.model.movie.MovieInfo
import com.teamfilmo.filmo.domain.bookmark.DeleteBookmarkUseCase
import com.teamfilmo.filmo.domain.bookmark.GetBookmarkLIstUseCase
import com.teamfilmo.filmo.domain.bookmark.RegistBookmarkUseCase
import com.teamfilmo.filmo.domain.like.CancelLikeUseCase
import com.teamfilmo.filmo.domain.like.CheckLikeStateUseCase
import com.teamfilmo.filmo.domain.like.RegistLikeUseCase
import com.teamfilmo.filmo.domain.movie.GetMovieImageUseCase
import com.teamfilmo.filmo.domain.movie.GetUpcomingMovieUseCase
import com.teamfilmo.filmo.domain.report.GetReportListUseCase
import com.teamfilmo.filmo.model.report.ReportItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

data class AllReportLikeState(
    val reportId: String = "",
    var likeCount: Int = 0,
)

data class AllReportBookmarkState(
    val reportId: String = "",
    val bookmarkId: Long = 0L,
    var isBookmarked: Boolean = false,
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
        private val deleteBookmarkUseCase: DeleteBookmarkUseCase,
        private val getUpcomingMovieUseCase: GetUpcomingMovieUseCase,
        private val getMovieImageUseCase: GetMovieImageUseCase,
    ) : BaseViewModel<AllMovieReportEffect, AllMovieReportEvent>() {
        init {
            fetchAllMovieReportList()
        }

        private var imageUriList: ArrayList<String> = arrayListOf()
        private val _upcomingMovieList = MutableStateFlow<List<MovieInfo>>(emptyList())
        private val _allMovieReportList = MutableStateFlow<List<ReportItem>>(emptyList())

        private val _likeState = MutableStateFlow<List<AllReportLikeState>>(emptyList())
        val likeState: StateFlow<List<AllReportLikeState>> = _likeState.asStateFlow()

        private val bookmarkState = MutableStateFlow<List<AllReportBookmarkState>>(emptyList())

        override fun handleEvent(event: AllMovieReportEvent) {
            when (event) {
                is AllMovieReportEvent.ClickLike -> toggleLike(event.reportId)
                is AllMovieReportEvent.ClickBookmark -> toggleBookmark(event.reportId)
                is AllMovieReportEvent.RefreshReport -> fetchAllMovieReportList()
                else -> {}
            }
        }

    /*
    영화 api : 최신 영화 정보 리스트 가져오기
     */
        private fun getUpcomingMovieList() {
            viewModelScope.launch {
                val list = mutableListOf<MovieInfo>()

                getUpcomingMovieUseCase()
                    .take(3).collect {
                        Timber.d("movielist : $it")
                        it.take(3).map { movieResult ->
                            getMovieImageUseCase(movieResult.id).collect {
                                Timber.d("뷰모델 : $it")
                                MovieInfo(
                                    movieAge = 16,
                                    movieImage = it,
                                    movieName = movieResult.title,
                                    genres = movieResult.genres,
                                    id = movieResult.id,
                                ).apply {
                                    list.add(this)
                                }
                            }
                        }
                    }
                Timber.d("이미지 :  $list")
                _upcomingMovieList.value = list
            }
        }

        val upcomingMovieList: StateFlow<List<MovieInfo>> =
            _upcomingMovieList.stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = emptyList(),
            )

    /*
    감상문 리스트 가져온 후 결합 (좋아요, 북마크 여부)
     */
        private fun fetchAllMovieReportList() {
            viewModelScope.launch {
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
                    _likeState.value =
                        reports.map { reportItem ->
                            AllReportLikeState(
                                reportId = reportItem.reportId,
                                likeCount = reportItem.likeCount,
                            )
                        }
                    bookmarkState.value =
                        reports.map { reportItem ->
                            val bookmark = bookmarkList.find { it.reportId == reportItem.reportId }
                            AllReportBookmarkState(
                                reportId = reportItem.reportId,
                                bookmarkId = bookmark?.id ?: 0L,
                                isBookmarked = reportItem.isBookmark,
                            )
                        }
                    reports
                }.collect { reports ->
                    _allMovieReportList.value = reports
                    sendEffect(AllMovieReportEffect.RefreshReport(_allMovieReportList.value))
                    Timber.d("${_allMovieReportList.value}")
                }
            }
            getUpcomingMovieList()
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
            _likeState.update { uiStateList ->
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
    북마크 등록 , 삭제
     */
        private fun registerBookmark(reportId: String) {
            viewModelScope.launch {
                val result = registBookmarkUseCase(reportId).first()
                if (result != null) {
                    bookmarkState.update { stateList ->
                        stateList.map { bookmark ->
                            if (bookmark.reportId == reportId) {
                                bookmark.copy(
                                    bookmarkId = result.id,
                                    isBookmarked = true,
                                )
                            } else {
                                bookmark
                            }
                        }
                    }
                }
                sendEffect(AllMovieReportEffect.RegistBookmark(reportId))
                Timber.d("bookmark regist ${bookmarkState.value} ")
            }
        }

        private fun deleteBookmark(
            reportId: String,
            bookmarkId: Long,
        ) {
            viewModelScope.launch {
                bookmarkState.update { stateList ->
                    stateList.map { bookmarkState ->
                        if (bookmarkState.bookmarkId == bookmarkId) {
                            bookmarkState.copy(
                                bookmarkId = bookmarkState.bookmarkId,
                                isBookmarked = false,
                            )
                        } else {
                            bookmarkState
                        }
                    }
                }
                deleteBookmarkUseCase(bookmarkId = bookmarkId).collect()
                sendEffect(AllMovieReportEffect.DeleteBookmark(reportId))
            }
        }

        private fun toggleBookmark(
            reportId: String,
        ) {
            val bookmark = bookmarkState.value.find { it.reportId == reportId }
            Timber.d("bookmark :  $bookmark")
            if (bookmark != null) {
                if (bookmark.isBookmarked) {
                    deleteBookmark(reportId, bookmark.bookmarkId)
                } else {
                    registerBookmark(reportId)
                }
            }
        }
    }
