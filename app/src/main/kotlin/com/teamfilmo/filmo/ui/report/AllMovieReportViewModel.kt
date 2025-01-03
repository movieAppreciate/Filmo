package com.teamfilmo.filmo.ui.report

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import com.teamfilmo.filmo.data.remote.model.like.CheckLikeResponse
import com.teamfilmo.filmo.data.remote.model.like.SaveLikeRequest
import com.teamfilmo.filmo.data.remote.model.like.SaveLikeResponse
import com.teamfilmo.filmo.data.remote.model.movie.MovieInfo
import com.teamfilmo.filmo.data.remote.model.movie.detail.response.DetailMovieResponse
import com.teamfilmo.filmo.data.remote.model.report.all.ReportItem
import com.teamfilmo.filmo.domain.bookmark.DeleteBookmarkUseCase
import com.teamfilmo.filmo.domain.bookmark.GetBookmarkLIstUseCase
import com.teamfilmo.filmo.domain.bookmark.RegistBookmarkUseCase
import com.teamfilmo.filmo.domain.like.CancelLikeUseCase
import com.teamfilmo.filmo.domain.like.CheckLikeStateUseCase
import com.teamfilmo.filmo.domain.like.CountLikeUseCase
import com.teamfilmo.filmo.domain.like.SaveLikeUseCase
import com.teamfilmo.filmo.domain.movie.GetUpcomingMovieUseCase
import com.teamfilmo.filmo.domain.movie.detail.GetMovieNameUseCase
import com.teamfilmo.filmo.domain.movie.detail.SearchMovieDetailUseCase
import com.teamfilmo.filmo.domain.report.GetReportListUseCase
import com.teamfilmo.filmo.domain.report.GetReportUseCase
import com.teamfilmo.filmo.domain.repository.UserPreferencesRepository
import com.teamfilmo.filmo.domain.user.GetUserInfoUseCase
import com.teamfilmo.filmo.ui.report.paging.ReportPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AllReportLikeState(
    val reportId: String = "",
    val likeId: String = "",
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
        private val userPreferencesRepository: UserPreferencesRepository,
        private val getUserInfoUserCase: GetUserInfoUseCase,
        private val countLikeUseCase: CountLikeUseCase,
        private val getReportUseCase: GetReportUseCase,
        private val getMovieNameUseCase: GetMovieNameUseCase,
        private val getReportListUseCase: GetReportListUseCase,
        private val getBookmarkListUseCase: GetBookmarkLIstUseCase,
        private val checkLikeStateUseCase: CheckLikeStateUseCase,
        private val registerLikeUseCase: SaveLikeUseCase,
        private val cancelLikeUseCase: CancelLikeUseCase,
        private val registBookmarkUseCase: RegistBookmarkUseCase,
        private val deleteBookmarkUseCase: DeleteBookmarkUseCase,
        private val getUpcomingMovieUseCase: GetUpcomingMovieUseCase,
        private val searchMovieDetailUseCase: SearchMovieDetailUseCase,
    ) : BaseViewModel<AllMovieReportEffect, AllMovieReportEvent>() {
        init {
            getMovieReports()
            getUpcomingMovieList()
        }

    /*
    영화 상세 정보
     */

        private val _movieDetailResponse = MutableStateFlow(DetailMovieResponse())
        val movieDetailResponse = _movieDetailResponse.asStateFlow()

        /*
        좋아요 체크
         */
        private val _checkLikeResponse = MutableStateFlow(CheckLikeResponse())
        val checkLikeResponse: StateFlow<CheckLikeResponse> = _checkLikeResponse

        /*
         좋아요 수 변수
         */
        private val _likeCount = MutableStateFlow(0)
        val likeCount: StateFlow<Int> = _likeCount

        /*
        좋아요 저장 변수
         */
        private val _saveLikeResponse = MutableStateFlow(SaveLikeResponse())
        val saveLikeResponse: StateFlow<SaveLikeResponse> = _saveLikeResponse

        /*
        감상문 페이징
         */
        private val _pagingData = MutableStateFlow<PagingData<ReportItem>>(PagingData.empty())
        val pagingData = _pagingData.asStateFlow()

        private val _movieName = MutableStateFlow<String>("")
        val movieName: StateFlow<String> = _movieName

        private val _upcomingMovieList = MutableStateFlow<List<MovieInfo>>(emptyList())

        private val _likeState = MutableStateFlow<List<AllReportLikeState>>(emptyList())
        val likeState: StateFlow<List<AllReportLikeState>> = _likeState.asStateFlow()

        private val bookmarkState = MutableStateFlow<List<AllReportBookmarkState>>(emptyList())

        override fun handleEvent(event: AllMovieReportEvent) {
            when (event) {
                is AllMovieReportEvent.LoadReport -> {
                }
                is AllMovieReportEvent.ClickLike -> toggleLike(event.reportId)
                is AllMovieReportEvent.ClickBookmark -> toggleBookmark(event.reportId)
                // 새로 고침
                is AllMovieReportEvent.RefreshReport -> getMovieReports()
                else -> {}
            }
        }

        // 영화 상세 정보
        private fun searchMovieDetail(movieId: Int) {
            viewModelScope.launch {
                searchMovieDetailUseCase(movieId).collect {
                    if (it != null) {
                        _movieDetailResponse.value = it
                    }
                }
            }
        }

        // 감상문 페이징
        private fun getMovieReports() {
            viewModelScope.launch(Dispatchers.IO) {
                Pager(
                    config =
                        PagingConfig(
                            pageSize = 20,
                            enablePlaceholders = false,
                            prefetchDistance = 3,
                        ),
                    pagingSourceFactory = {
                        ReportPagingSource(
                            getReportUseCase,
                            getMovieNameUseCase,
                            getReportListUseCase,
                            getBookmarkListUseCase,
                            checkLikeStateUseCase,
                        )
                    },
                ).flow.cachedIn(viewModelScope).collect {
                    _pagingData.value = it
                }
            }
        }

        // 영화 api : 최신 영화 정보 리스트 가져오기
        private fun getUpcomingMovieList() {
            // todo :각 영화에 대한 영화 정보 가져오기
            viewModelScope.launch(Dispatchers.IO) {
                val list = mutableListOf<MovieInfo>()
                getUpcomingMovieUseCase()
                    .take(3)
                    .collect {
                        it.take(3).map { movieResult ->
                            val imageBaseUrl = "https://image.tmdb.org/t/p/original"
                            MovieInfo(
                                movieImage = imageBaseUrl + movieResult.posterPath,
                                movieName = movieResult.title,
                                genres = movieResult.genres,
                                id = movieResult.id,
                            ).apply {
                                list.add(this)
                            }
                        }
                    }
                _upcomingMovieList.value = list
            }
        }

        val upcomingMovieList: StateFlow<List<MovieInfo>> = _upcomingMovieList

    /*
     영화 이름

    < 기존 코드 문제 >
    getName : 비동기 작업을 viewModel.Scope.launch로 수행하는데, 이 함수가 값을 반환하기 전에 함수가
    종료되기 때문에 결과적으로 movieName 값이 반환되지 않는다.

    launch 는 단순히 비동기 작업을 시작하지만 , 호출하는 부분에서는 그 결과를 기다리지 않기 때문에
    값을 반환하지 않으며 영화 이름을 가져올 때까지 기다리지 않고 호출 코드가 계속 실행되어 영화 이름이 UI에 반영되지 않았다.

    < 수정 >
    async와 await를 통해 영화 이름을 비동기적으로 처리한 후 reportList를 생성하기 전에
    모든 비동기 작업이 완료되도록 보장한다.

    - async : 비동기자업을 수행하는 코루틴을 반환
    - await : 그 결과를 기다린 후 반환

    이렇게 함으로써 모든 데이터를 준비한 후 리포트 리스트를 구성하게 되어 영화 이름이 제대로 표시된다.

    <async와 await의 역할>
    - async : 비동기적으로 값을 반환하는 코루틴을 시작하는 함수
    - await : 그 코루틴이 끝날 때까지 기다리고 그 결과를 반환
    이 두 함수를 통해 비동기 작업을 순차적으로 처리할 수 있다.

    각 reportItem 마다 영화 이름을 가져오는 비동기 작업을 async로 처리하고
    그 결과를 awaitAll()로 모두 기다린 후 리스트를 생성한다.

    <firstOrNull 과 collect의 차이>
    - firstOrNull : 첫번째 값을 기다리다가 값을 받으면 바로 반환하고, 값이 없으면 null을 반환하는 비동기 함수
                     흐름이 단 한번의 값을 받는 경우에 유용한다.

    - collect : flow의 모든 데이터를 수집할 때 사용하는 함수,
                 연속적인 값을 처리할 때 유용
    collect는 흐름이 완료될 때까지 모든 값을 처리
    firstOrNull은 첫번째 값만을 처리한다.
*/

        // 좋아요 등록
        private fun saveLike(reportId: String) {
            viewModelScope.launch {
                registerLikeUseCase(
                    SaveLikeRequest(
                        targetId = reportId,
                        type = "report",
                    ),
                ).collect {
                    if (it == null) {
                        return@collect
                    } else {
                        _saveLikeResponse.value = it
                        updateLikeCount(reportId, true)

                        val updatedLikeState =
                            _checkLikeResponse.value.copy(
                                likeId = it.likeId,
                                isLike = true,
                            )

                        _checkLikeResponse.value = updatedLikeState
                        sendEffect(AllMovieReportEffect.RegistLike(reportId))
                    }
                }
            }
        }

/*
좋아요 취소
 */
        private fun cancelLike(likeId: String) {
            viewModelScope.launch {
                if (_checkLikeResponse.value.likeId == null) return@launch
                cancelLikeUseCase(likeId).collect {
                    if (it == null) return@collect
                    val updatedLikeState =
                        _checkLikeResponse.value.copy(
                            likeId = null,
                            isLike = false,
                        )
                    _checkLikeResponse.value = updatedLikeState
                    updateLikeCount(reportId = _saveLikeResponse.value.targetId, false)
                    sendEffect(AllMovieReportEffect.CancelLike(_saveLikeResponse.value.targetId))
                }
            }
        }

        // 좋아요 토글
        private fun toggleLike(
            targetId: String,
            type: String = "report",
        ) {
            viewModelScope.launch {
                checkLikeStateUseCase(targetId, type).collect {
                    if (it != null) {
                        _checkLikeResponse.value = it
                        if (it.isLike) {
                            if (it.likeId != null) cancelLike(it.likeId)
                        } else {
                            saveLike(targetId)
                        }
                    }
                }
            }
        }

        // 좋아요 수 업데이트
        private fun updateLikeCount(
            reportId: String,
            isLiked: Boolean,
        ) {
            viewModelScope.launch {
                countLikeUseCase(reportId).collect {
                    if (it != null) {
                        _likeCount.value = it.countLike
                        sendEffect(AllMovieReportEffect.CountLike(reportId, _likeCount.value))
                    }
                }
            }
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
                                    bookmarkId = result.bookmarkId,
                                    isBookmarked = true,
                                )
                            } else {
                                bookmark
                            }
                        }
                    }
                }
                sendEffect(AllMovieReportEffect.RegistBookmark(reportId))
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
            if (bookmark != null) {
                if (bookmark.isBookmarked) {
                    deleteBookmark(reportId, bookmark.bookmarkId)
                }
            } else {
                registerBookmark(reportId)
            }
        }
    }
