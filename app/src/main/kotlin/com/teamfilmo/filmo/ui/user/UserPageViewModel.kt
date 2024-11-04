package com.teamfilmo.filmo.ui.user

import androidx.lifecycle.viewModelScope
import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import com.teamfilmo.filmo.data.remote.model.follow.check.CheckIsFollowResponse
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
class UserPageViewModel
    @Inject
    constructor(
        private val cancelFollowUseCase: CancelFollowUseCase,
        private val saveFollowUseCase: SaveFollowUseCase,
        private val checkIsFollowUseCase: CheckIsFollowUseCase,
        private val getUserInfoUseCase: GetUserInfoUseCase,
        private val getMovieNameUseCase: GetMovieNameUseCase,
        private val getReportUseCase: GetReportUseCase,
        private val searchUserReportListUseCase: GetUserReportListUseCase,
        private val countFollowUseCase: CountFollowUseCase,
    ) : BaseViewModel<UserPageEffect, UserPageEvent>() {
        init {
        }

        override fun handleEvent(event: UserPageEvent) {
            when (event) {
                is UserPageEvent.GetUserReportList -> {
                    getUserInfo(event.targetId)
                    getUserReportList(event.targetId)
                    getFollowCount(event.targetId)
                }
                is UserPageEvent.ClickFollow -> {
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
        private val _checkIsFollowResponse = MutableStateFlow(CheckIsFollowResponse())
        val checkIsFollowResponse: StateFlow<CheckIsFollowResponse> = _checkIsFollowResponse

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
    todo : 내가  유저의 팔로워 중 팔로우 하는 사람이 있는지 구현 필요
     */
        private fun checkMutualFollowers() {
        }

    /*
 내가 사용자를 팔로우 등록(팔로워 증가해야함)
     */
        private fun saveFollow() {
            viewModelScope.launch {
                saveFollowUseCase(_userInfo.value.userId).collect {
                    _followInfo.value = it
                    val updatedFollowResponse = _checkIsFollowResponse.value.copy(isFollowing = true)
                    _checkIsFollowResponse.value = updatedFollowResponse
                    _followerCount.value += 1
                    sendEffect(UserPageEffect.SaveFollow)
                }
            }
        }

    /*
  내가 사용자를 팔로우 취소( 팔로워 감소해야함)
     */
        private fun cancelFollow() {
            viewModelScope.launch {
                cancelFollowUseCase(_checkIsFollowResponse.value.followId).collect {
                    val updatedFollowResponse = _checkIsFollowResponse.value.copy(isFollowing = false)
                    _checkIsFollowResponse.value = updatedFollowResponse
                    _followerCount.value -= 1
                    sendEffect(UserPageEffect.CancelFollow)
                }
            }
        }

    /*
     팔로우 토글
     */
        private fun toggleFollow() {
            if (_checkIsFollowResponse.value.isFollowing) {
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
                        if (it.isFollowing) {
                            sendEffect(UserPageEffect.IsFollow)
                        } else {
                            sendEffect(UserPageEffect.IsNotFollow)
                        }
                    }
                }
            }
        }

    /*
    유저 정보
     */
        private fun getUserInfo(targetId: String?) {
            viewModelScope.launch {
                getUserInfoUseCase(targetId).collect {
                    if (it != null) {
                        _userInfo.value = it
                        getFollowCount(it.userId)
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
                searchUserReportListUseCase(targetId)
                    .flatMapLatest { reportList ->
                        val reportFlows =
                            reportList.map { reportItem ->
                                getReportUseCase(reportItem.reportId).flatMapLatest { detailReportItem ->
                                    getMovieNameUseCase(DetailMovieRequest(detailReportItem?.movieId.toString())).map { movieName ->
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

        private fun getFollowCount(otherUserId: String) {
            viewModelScope.launch {
                countFollowUseCase(otherUserId = otherUserId).collect {
                    if (it != null) {
                        _followingCount.value = it.countFollowing
                        _followerCount.value = it.countFollower
                    }
                }
            }
        }
    }
