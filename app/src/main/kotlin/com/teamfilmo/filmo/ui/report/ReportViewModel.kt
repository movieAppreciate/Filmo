package com.teamfilmo.filmo.ui.report

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.teamfilmo.filmo.domain.repository.BookmarkRepository
import com.teamfilmo.filmo.domain.repository.LikeRepository
import com.teamfilmo.filmo.domain.repository.MovieRepository
import com.teamfilmo.filmo.domain.repository.ReportRepository
import com.teamfilmo.filmo.ui.model.bookmark.BookmarkCount
import com.teamfilmo.filmo.ui.model.bookmark.BookmarkList
import com.teamfilmo.filmo.ui.model.bookmark.BookmarkResponse
import com.teamfilmo.filmo.ui.model.movie.DetailMovieResponse
import com.teamfilmo.filmo.ui.model.movie.MovieResponse
import com.teamfilmo.filmo.ui.model.report.Report
import com.teamfilmo.filmo.ui.model.report.ReportInfo
import com.teamfilmo.filmo.ui.model.report.ReportItem
import com.teamfilmo.filmo.ui.model.report.ReportList
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

data class UiState(
    var reportId: String = "",
    var isLiked: Boolean = false,
    var likeCount: Int = 0,
    val isBookmarked: Boolean = false,
    val bookmarkCount: Int = 0,
)

data class LikeState(
    val reportId: String = "",
    val isLiked: Boolean = false,
    val likeCount: Int = 0,
)

data class BookmarkState(
    val reportId: String = "",
    val isBookmarked: Boolean = false,
    val bookmarkCount: Int = 0,
)

@HiltViewModel
class ReportViewModel
    @Inject
    constructor(
        private val reportRepository: ReportRepository,
        private val movieRepository: MovieRepository,
        private val likeRepository: LikeRepository,
        private val bookmarkRepository: BookmarkRepository,
    ) :
    ViewModel() {
        private var genreIdList = listOf<String>()
        val uiState = MutableLiveData<UiState>()

        val likeState: LiveData<LikeState> =
            uiState.map {
                LikeState(it.reportId, it.isLiked, it.likeCount)
            }

        val bookmarkState: LiveData<BookmarkState> =
            uiState.map {
                BookmarkState(it.reportId, it.isBookmarked)
            }

        private var sortRecommendList: List<Report> = listOf()
        private var _recommendList = MutableLiveData<List<Report>>()
        val recommendList: LiveData<List<Report>>
            get() = _recommendList

        private var _bookmarkList =
            MutableLiveData<List<BookmarkResponse>>()
        val bookmarkList: LiveData<List<BookmarkResponse>>
            get() = _bookmarkList

        private var likeList: MutableList<Boolean> = mutableListOf()

        private var _report =
            MutableLiveData<List<ReportItem>>()

        val report: LiveData<List<ReportItem>>
            get() = _report

        private var reportList: MutableList<Report> = mutableListOf()
        private var _filteredReportList = MutableLiveData<List<ReportItem>>()
        val filteredReportList: LiveData<List<ReportItem>> get() = _filteredReportList

        fun updateLike(reportId: String) {
            viewModelScope.launch {
                val result = checkLike(reportId)
                if (result.isSuccess) {
                    val response = result.getOrThrow()
                    if (response) {
                        cancelLike(reportId)
                    } else {
                        registLike(reportId)
                    }
                    updateLikeCount(reportId)
                }
            }
        }

        fun toggleBookmark(reportId: String) {
            viewModelScope.launch {
                getBookmarkResponse()
                if (_bookmarkList.value != null) {
                    val bookmark = _bookmarkList.value?.find { it.reportId == reportId }
                    Log.d("북마크 토글", bookmark.toString())
                    if (bookmark != null) {
                        deleteBookmark(bookmark.bookmarkId)
                    } else {
                        registBookmark(reportId)
                    }
                    getBookmarkResponse()
                }
            }
        }

        private suspend fun registBookmark(reportId: String): Result<BookmarkResponse> {
            val currentState = uiState.value ?: UiState()
            val newState =
                currentState.copy(
                    reportId = reportId,
                    isBookmarked = true,
                )
            uiState.value = newState
            Log.d("ui state 북마크 변경", uiState.value.toString())
            return bookmarkRepository.registBookmark(reportId)
        }

        suspend fun searchMovieList(movie: String): Result<MovieResponse> {
            return movieRepository.searchList(movie, 1)
        }

    /*
    북마크
     */
        suspend fun getBookmarkResponse() {
            val result = getBookmark()
            if (result.isSuccess) {
                _bookmarkList.value = result.getOrThrow().bookmarkList
            }
        }

        private suspend fun getBookmark(): Result<BookmarkList> {
            return bookmarkRepository.getBookmarkList()
        }

        suspend fun getBookmarkCount(reportId: String): Result<BookmarkCount> {
            return bookmarkRepository.getBookmarkCount(reportId)
        }

        private suspend fun deleteBookmark(bookmarkId: Int): Result<String> {
            val currentState = uiState.value ?: UiState()
            val newState =
                currentState.copy(
                    isBookmarked = false,
                )
            uiState.value = newState

            return bookmarkRepository.deleteBookmark(bookmarkId)
        }

    /*
    감상문
     */
        private suspend fun searchReport(): Result<ReportInfo> {
            return reportRepository.searchReport()
        }

        private fun mapForReportItem(
            reportList: List<ReportList>,
            likeList: MutableList<Boolean>,
        ): List<ReportItem> {
            return reportList.mapIndexed { index, reportItem ->
                ReportItem(
                    reportId = reportItem.reportId,
                    title = reportItem.title,
                    content = reportItem.content,
                    createDate = reportItem.createDate,
                    imageUrl = reportItem.imageUrl,
                    nickname = reportItem.nickname,
                    likeCount = reportItem.likeCount,
                    replyCount = reportItem.replyCount,
                    bookmarkCount = reportItem.bookmarkCount,
                    isLiked = likeList[index],
                    genreIds = genreIdList,
                )
            }
        }

        private fun mapForMovieItem(
            reportId: String,
            movie: String,
            genreList: List<String>,
        ) {
        }

        fun requestReport() {
            viewModelScope.launch {
                val result = searchReport()
                if (result.isSuccess) {
                    val response = result.getOrNull()
                    if (response != null) {
                        response.reportList.forEach {
                            val likeResult = checkLike(it.reportId)
                            if (likeResult.isSuccess) {
                                likeResult.getOrNull()?.let { isLiked ->
                                    likeList.add(isLiked)
                                }
                                getGenre(it.reportId)
                            }
                        }

                        _report.value = mapForReportItem(response.reportList, likeList)
                        getReportList()
                    }
                } else {
                    result.getOrThrow()
                }
            }
        }

        private fun getReportList() {
            viewModelScope.launch {
                _report.value?.forEach {
                    val result = getReport(it.reportId)
                    if (result.isSuccess) {
                        result.getOrNull()?.let { it1 -> reportList.add(it1) }
                    } else {
                    }
                }
                sortRecommendReport()
            }
        }

        private suspend fun getReport(reportId: String): Result<Report> {
            return reportRepository.getReport(reportId)
        }

    /*
    좋아요
     */
        private suspend fun registLike(reportId: String): Result<String> {
            val currentState = uiState.value ?: UiState()
            val newState =
                currentState.copy(
                    reportId = reportId,
                    isLiked = true,
                )
            uiState.value = newState
            Log.d("좋아요 뷰모델", "$reportId 좋아요 등록")
            return likeRepository.registLike(reportId)
        }

        private suspend fun cancelLike(reportId: String): Result<String> {
            val currentState = uiState.value ?: UiState()
            val newState =
                currentState.copy(
                    reportId = reportId,
                    isLiked = false,
                )
            uiState.value = newState
            Log.d("좋아요 뷰모델", "취소")
            return likeRepository.cancleLike(reportId)
        }

        private suspend fun updateLikeCount(reportId: String) {
            val response = countLike(reportId)
            if (response.isSuccess) {
                val currentState = uiState.value ?: UiState()
                val newState =
                    currentState.copy(
                        reportId = reportId,
                        likeCount = response.getOrThrow(),
                    )
                uiState.value = newState
            } else {
                Log.d("update like count failed", response.exceptionOrNull().toString())
            }
        }

        private suspend fun checkLike(reportId: String): Result<Boolean> {
            return likeRepository.checkLike(reportId)
        }

        private suspend fun countLike(reportId: String): Result<Int> {
            return likeRepository.countLike(reportId)
        }

        /**
         * 추천 감상문 데이터 처리 로직
         */

        private fun <T> List<T>.getRandomElements(count: Int): List<T> {
            require(count in 0..size)
            return this.shuffled().take(count)
        }

        private fun sortRecommendReport() {
            viewModelScope.launch {
                val sorted = reportList.sortedBy { it.viewCount.plus(it.likeCount) }
                // 좋아요 수 + 조회수를 기준으로 정렬하기
                if (sorted.size >= 20) {
                    sorted.take(20)
                    sortRecommendList = sorted.getRandomElements(5)
                } else if (sorted.size < 5) {
                    sortRecommendList = sorted
                } else {
                    sortRecommendList = sorted.getRandomElements(5)
                }
                _recommendList.value = sortRecommendList
            }
        }

    /*
    장르 버튼 기능 구현
     */
        suspend fun filterGenreReport(genre: String) {
            val list = mutableListOf<ReportItem>()
            reportList.forEach { item ->
                val report = _report.value?.find { it.reportId == item.reportId }
                val result = searchMovieDetail(item.movieId)
                if (result.isSuccess) {
                    result.getOrThrow().apply {
                        val filteredItem = this.genres.find { it.name == genre }

                        if (filteredItem != null) {
                            if (report != null) {
                                list.add(report)
                            }
                        }
                    }
                } else {
                    Log.d("filter 실패", result.exceptionOrNull().toString())
                }
            }
            _filteredReportList.value = list
        }

        private fun getGenre(reportId: String) {
            val movieId = reportList.find { it.reportId == reportId }?.movieId
            val list = mutableListOf<String>()
            viewModelScope.launch {
                val result = movieId?.let { searchMovieDetail(it) }
                if (result != null) {
                    if (result.isSuccess) {
                        result.getOrThrow().genres.forEach {
                            list.add(it.name)
                        }
                    }
                }
                genreIdList = list
            }
        }

        private suspend fun searchMovieDetail(movieId: Int): Result<DetailMovieResponse> {
            return movieRepository.searchDetail(movieId = movieId)
        }
    }
