package com.teamfilmo.filmo.ui.body

import androidx.lifecycle.viewModelScope
import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import com.teamfilmo.filmo.data.remote.model.follow.save.SaveFollowRequest
import com.teamfilmo.filmo.data.remote.model.follow.save.SaveFollowResponse
import com.teamfilmo.filmo.data.remote.model.movie.detail.response.DetailMovieResponse
import com.teamfilmo.filmo.data.remote.model.report.get.GetReportResponse
import com.teamfilmo.filmo.data.remote.model.user.UserResponse
import com.teamfilmo.filmo.domain.follow.CancelFollowUseCase
import com.teamfilmo.filmo.domain.follow.CheckIsFollowUseCase
import com.teamfilmo.filmo.domain.follow.SaveFollowUseCase
import com.teamfilmo.filmo.domain.movie.detail.SearchMovieDetailUseCase
import com.teamfilmo.filmo.domain.report.DeleteReportUseCase
import com.teamfilmo.filmo.domain.report.GetReportUseCase
import com.teamfilmo.filmo.domain.user.GetUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class BodyMovieReportViewModel
    @Inject
    constructor(
        private val checkIsFollowUseCase: CheckIsFollowUseCase,
        private val cancelFollowUseCase: CancelFollowUseCase,
        private val saveFollowUseCase: SaveFollowUseCase,
        private val getUserInfoUseCase: GetUserInfoUseCase,
        private val deleteReportUseCase: DeleteReportUseCase,
        private val getReportUseCase: GetReportUseCase,
        private val searchMovieDetailUseCase: SearchMovieDetailUseCase,
    ) : BaseViewModel<BodyMovieReportEffect, BodyMovieReportEvent>() {
        init {
            // 현재 로그인한 유저 정보
            viewModelScope.launch {
                getUserInfoUseCase().collect {
                    if (it != null) {
                        _userInfo.value = it
                    }
                }
            }

            // 팔로우 여부 확인
            viewModelScope.launch {
            }
        }

    /*
    팔로우 정보
     */
        private val _followInfo = MutableStateFlow(SaveFollowResponse("", "", ""))
        val followInfo: StateFlow<SaveFollowResponse> = _followInfo

        /*
        본인의 게시글인기?
         */
        private val _isMyPost = MutableStateFlow(false)
        val isMyPost: StateFlow<Boolean> = _isMyPost

        /*
        현재 로그인한 유저 정보
         */
        private val _userInfo = MutableStateFlow(UserResponse("", "", "", "", "", "", "", "", ""))
        val userInfo: StateFlow<UserResponse> = _userInfo

        /*
  영화 상세 내용
         */
        private val _movieContent = MutableStateFlow("")
        val movieContent: StateFlow<String> = _movieContent

        /*
    영화 상세 정보
         */
        private val _movieDetailInfo =
            MutableStateFlow(
                DetailMovieResponse(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, ""),
            )

        val movieDetailInfo = _movieDetailInfo.asStateFlow()

    /*
    개별 감상문 정보
     */
        private val _getReportResponse =
            MutableStateFlow(
                GetReportResponse(
                    reportId = "",
                    title = "",
                    content = "",
                    userId = "",
                    movieId = 0,
                    tagString = "",
                    complaintCount = 0,
                    replyCount = 0,
                    likeCount = 0,
                    viewCount = 0,
                    imageUrl = "",
                    createDate = "",
                    lastModifiedDate = "",
                ),
            )
        val getReportResponse: StateFlow<GetReportResponse> = _getReportResponse.asStateFlow()

    /*
      팔로우 토글
     */
        private fun toggleFollow() {
            if (_checkIsFollowResponse.value) {
                cancelFollow()
                sendEffect(BodyMovieReportEffect.CancelFollow)
            } else {
                saveFollow()
                sendEffect(BodyMovieReportEffect.SaveFollow)
            }
        }

    /*
    팔로우 여부
     */
        private val _checkIsFollowResponse = MutableStateFlow(false)
        val checkIsFollowResponse: StateFlow<Boolean> = _checkIsFollowResponse

    /*
 팔로우 등록
     */
        private fun saveFollow() {
            viewModelScope.launch {
                val saveFollowRequest =
                    SaveFollowRequest(
                        _getReportResponse.value.userId,
                    )
                saveFollowUseCase(_getReportResponse.value.userId).collect {
                    _followInfo.value = it
                    _checkIsFollowResponse.value = true
                    sendEffect(BodyMovieReportEffect.SaveFollow)
                }
            }
        }

    /*
    팔로우 취소
     */
        private fun cancelFollow() {
            viewModelScope.launch {
                cancelFollowUseCase(_followInfo.value.followId).collect {
                    _checkIsFollowResponse.value = false
                    sendEffect(BodyMovieReportEffect.CancelFollow)
                }
            }
        }

    /*
    팔로우 여부 검사
     */
        private fun checkIsFollow() {
            viewModelScope.launch {
                checkIsFollowUseCase(_getReportResponse.value.userId).collect {
                    if (it != null) {
                        _checkIsFollowResponse.value = it
                    }
                }
            }
        }

        /*
       감상문 수정
         */
        private fun updateReport() {
        }

    /*
    영화 줄거리
     */
        private fun getMovieContent() {
            _movieContent.value = _movieDetailInfo.value.overview.toString()
        }
    /*
    영화 정보
     */

        private fun searchMovieDetail(movieId: Int) {
            viewModelScope.launch {
                searchMovieDetailUseCase(movieId).collect {
                    if (it != null) {
                        _movieDetailInfo.value = it
                        Timber.d("영화 연령 정보 : ${_movieDetailInfo.value.certification}")
                        sendEffect(BodyMovieReportEffect.ShowMovieInfo)
                    }
                }
            }
        }

        private fun deleteReport(reportId: String) {
            viewModelScope.launch {
                deleteReportUseCase(reportId).collect {
                    Timber.d("삭제 결과 :$it")
                    sendEffect(BodyMovieReportEffect.DeleteReport)
                }
            }
        }

        private fun getReport(reportId: String) {
            viewModelScope.launch {
                getReportUseCase(reportId).collect {
                    if (it != null) {
                        _getReportResponse.value = it
                        checkIsFollow()
                        if (_userInfo.value.userId == _getReportResponse.value.userId) {
                            _isMyPost.value = true
                        } else {
                            // else 문을 적어주지 않으면 안되는 것이었다. 코드를 잘못 작성해줬음.
                            _isMyPost.value = false
                        }
                        sendEffect(BodyMovieReportEffect.ShowReport)
                    }
                }
            }
        }

        override fun handleEvent(event: BodyMovieReportEvent) {
            when (event) {
                is BodyMovieReportEvent.ClickFollow -> {
                    toggleFollow()
                }
                is BodyMovieReportEvent.ClickMoreButton -> {
                    getMovieContent()
                }
                is BodyMovieReportEvent.ShowReport -> {
                    getReport(event.reportId)
                }
                is BodyMovieReportEvent.ShowMovieInfo -> {
                    searchMovieDetail(event.movieId)
                }
                is BodyMovieReportEvent.UpdateReport -> {
                }
                is BodyMovieReportEvent.DeleteReport -> {
                    deleteReport(reportId = event.reportId)
                }
                else -> {}
            }
        }
    }
