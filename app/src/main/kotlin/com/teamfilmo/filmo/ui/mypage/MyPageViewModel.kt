package com.teamfilmo.filmo.ui.mypage

import androidx.lifecycle.viewModelScope
import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import com.teamfilmo.filmo.data.remote.model.follow.save.SaveFollowResponse
import com.teamfilmo.filmo.data.remote.model.movie.detail.DetailMovieRequest
import com.teamfilmo.filmo.data.remote.model.report.MyPageReportItem
import com.teamfilmo.filmo.data.remote.model.report.get.GetReportResponse
import com.teamfilmo.filmo.data.remote.model.user.UserResponse
import com.teamfilmo.filmo.domain.follow.CancelFollowUseCase
import com.teamfilmo.filmo.domain.follow.CheckIsFollowUseCase
import com.teamfilmo.filmo.domain.follow.CountFollowUseCase
import com.teamfilmo.filmo.domain.follow.SaveFollowUseCase
import com.teamfilmo.filmo.domain.movie.detail.GetMovieNameUseCase
import com.teamfilmo.filmo.domain.report.GetReportUseCase
import com.teamfilmo.filmo.domain.report.GetUserReportListUseCase
import com.teamfilmo.filmo.domain.user.GetUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class MyPageViewModel
    @Inject
    constructor(
        private val cancelFollowUseCase: CancelFollowUseCase,
        private val saveFollowUseCase: SaveFollowUseCase,
        private val checkIsFollowUseCase: CheckIsFollowUseCase,
        private val getUserInfoUseCase: GetUserInfoUseCase,
        private val getMovieNameUseCase: GetMovieNameUseCase,
        private val getReportUseCase: GetReportUseCase,
        private val searchUserReportListUseCase: GetUserReportListUseCase,
        private val followCountFollowUseCase: CountFollowUseCase,
    ) : BaseViewModel<MyPageEffect, MyPageEvent>() {
        init {
            // 현재 로그인한 유저
            viewModelScope.launch {
                getUserInfoUseCase(null).collect {
                    if (it != null) {
                        _currentUser.value = it
                    }
                }
            }
        }

        override fun handleEvent(event: MyPageEvent) {
            when (event) {
                is MyPageEvent.ClickFollow -> {
                    toggleFollow()
                }
            }
        }

    /*
       팔로우 정보
     */
        private val _followInfo = MutableStateFlow(SaveFollowResponse("", "", ""))
        val followInfo: StateFlow<SaveFollowResponse> = _followInfo

    /*
    팔로우 여부 확인
     */
        private val _checkIsFollowResponse = MutableStateFlow(false)
        val checkIsFollowResponse: StateFlow<Boolean> = _checkIsFollowResponse

    /*
    현재 로그인한 유저 정보
     */
        private val _currentUser = MutableStateFlow(UserResponse())
        val currentUser: StateFlow<UserResponse> = _currentUser

        /*
        사용자 정보
         */
        private val _userInfo = MutableStateFlow(UserResponse())
        val userInfo: StateFlow<UserResponse> = _userInfo

        /*
    감상문 상세 정보
         */
        private val _detailReportInfo =
            MutableStateFlow<GetReportResponse>(
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
                    imageUrl = null,
                    createDate = "",
                    lastModifiedDate = "",
                ),
            )

        val detailReportInfo: StateFlow<GetReportResponse> = _detailReportInfo

    /*
        팔로우 수
     */
        private val _followerCount = MutableStateFlow(0)
        val followerCount: StateFlow<Int> = _followerCount.asStateFlow()

    /*
     팔로잉 수
     */
        private val _followingCount = MutableStateFlow(0)
        val followingCount: StateFlow<Int> = _followingCount.asStateFlow()

    /*
    감상문 정보
     */

        private val _userReportList = MutableStateFlow<List<MyPageReportItem>>(emptyList())
        val userReportList: StateFlow<List<MyPageReportItem>> = _userReportList

    /*
 팔로우 등록
     */
        private fun saveFollow() {
            viewModelScope.launch {
                saveFollowUseCase(_userInfo.value.userId).collect {
                    _followInfo.value = it
                    _checkIsFollowResponse.value = true
                    sendEffect(MyPageEffect.SaveFollow)
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
                    sendEffect(MyPageEffect.CancelFollow)
                }
            }
        }

    /*
     팔로우 토글
     */
        private fun toggleFollow() {
            if (_checkIsFollowResponse.value) {
                cancelFollow()
            } else {
                saveFollow()
            }
        }

    /*
    팔로우 여부 확인
     */

        fun checkIsFollow(userId: String) {
            viewModelScope.launch {
                checkIsFollowUseCase(userId).collect {
                    if (it != null) {
                        _checkIsFollowResponse.value = it
                        Timber.d("팔로우 여부 확인 : $it")
                        if (it) {
                            sendEffect(MyPageEffect.IsFollow)
                        } else {
                            sendEffect(MyPageEffect.IsNotFollow)
                        }
                    }
                }
            }
        }
    /*
    유저 정보
     */

        fun getUserInfo(targetId: String) {
            viewModelScope.launch {
                getUserInfoUseCase(targetId).collect {
                    if (it != null) {
                        _userInfo.value = it
                    }
                }
            }
        }

    /*
    감상문 정보 불러오기
     */
        @OptIn(ExperimentalCoroutinesApi::class)
        fun getUserReportList(targetId: String) {
            viewModelScope.launch {
                searchUserReportListUseCase(targetId).flatMapLatest { reportList ->
                    val reportFlows =
                        reportList.map { reportItem ->
                            getReportUseCase(reportItem.reportId).flatMapLatest { detailReportItem ->
                                getMovieNameUseCase(DetailMovieRequest(detailReportItem?.movieId.toString())).map {
                                        movieName ->
                                    MyPageReportItem(
                                        reportId = reportItem.reportId,
                                        movieName = movieName,
                                        reportTitle = reportItem.title,
                                        reportContent = reportItem.content,
                                    )
                                }
                            }
                        }
                    combine(reportFlows) { it.toList() }
                }.collect { combinedList ->
                    _userReportList.value = combinedList
                }
            }
        }

        fun getFollowCount(otherUserId: String) {
            viewModelScope.launch {
                followCountFollowUseCase(otherUserId = otherUserId).collect {
                    if (it != null) {
                        _followingCount.value = it.countFollowing
                    }
                    if (it != null) {
                        _followerCount.value = it.countFollower
                    }
                }
            }
        }
    }
