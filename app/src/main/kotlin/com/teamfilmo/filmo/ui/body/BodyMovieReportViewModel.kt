package com.teamfilmo.filmo.ui.body

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import com.teamfilmo.filmo.data.remote.entity.block.SaveBlockRequest
import com.teamfilmo.filmo.data.remote.entity.block.SaveBlockResponse
import com.teamfilmo.filmo.data.remote.entity.complaint.SaveComplaintRequest
import com.teamfilmo.filmo.data.remote.entity.follow.check.CheckIsFollowResponse
import com.teamfilmo.filmo.data.remote.entity.like.CheckLikeResponse
import com.teamfilmo.filmo.data.remote.entity.like.SaveLikeRequest
import com.teamfilmo.filmo.data.remote.entity.report.get.GetReportResponse
import com.teamfilmo.filmo.domain.block.SaveBlockUseCase
import com.teamfilmo.filmo.domain.complaint.SaveComplaintUseCase
import com.teamfilmo.filmo.domain.follow.CancelFollowUseCase
import com.teamfilmo.filmo.domain.follow.CheckIsFollowUseCase
import com.teamfilmo.filmo.domain.follow.SaveFollowUseCase
import com.teamfilmo.filmo.domain.like.CancelLikeUseCase
import com.teamfilmo.filmo.domain.like.CheckLikeStateUseCase
import com.teamfilmo.filmo.domain.like.CountLikeUseCase
import com.teamfilmo.filmo.domain.like.SaveLikeUseCase
import com.teamfilmo.filmo.domain.model.movie.DetailMovie
import com.teamfilmo.filmo.domain.model.user.UserInfo
import com.teamfilmo.filmo.domain.movie.detail.SearchMovieDetailUseCase
import com.teamfilmo.filmo.domain.report.DeleteReportUseCase
import com.teamfilmo.filmo.domain.report.GetReportUseCase
import com.teamfilmo.filmo.domain.repository.UserPreferencesRepository
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
        private val savedStateHandle: SavedStateHandle,
        private val userPreferencesRepository: UserPreferencesRepository,
        private val countLikeUseCase: CountLikeUseCase,
        private val checkLikeStateUseCase: CheckLikeStateUseCase,
        private val registerLikeUseCase: SaveLikeUseCase,
        private val cancelLikeUseCase: CancelLikeUseCase,
        private val saveBlockUseCase: SaveBlockUseCase,
        private val saveComplaintUseCase: SaveComplaintUseCase,
        private val checkIsFollowUseCase: CheckIsFollowUseCase,
        private val cancelFollowUseCase: CancelFollowUseCase,
        private val saveFollowUseCase: SaveFollowUseCase,
        private val getUserInfoUseCase: GetUserInfoUseCase,
        private val deleteReportUseCase: DeleteReportUseCase,
        private val getReportUseCase: GetReportUseCase,
        private val searchMovieDetailUseCase: SearchMovieDetailUseCase,
    ) : BaseViewModel<BodyMovieReportEffect, BodyMovieReportEvent>() {
        companion object {
            private const val KEY_REPORT_ID = "reportId" // args와 동일한 키 사용!
        }

        private val reportId: String = checkNotNull(savedStateHandle[KEY_REPORT_ID])

        init {
            // 데이터 로드
            getReport()
            // 현재 로그인한 유저 정보
            viewModelScope.launch {
                userPreferencesRepository.getUserInfo().collect {
                    if (it != null) {
                        userInfo = it
                    }
                }
            }
        }

        private var userNickName: String? = null

    /*
    좋아요 여부 변수
     */
        private val _checkLikeResponse = MutableStateFlow(CheckLikeResponse())
        val checkLikeResponse: StateFlow<CheckLikeResponse> = _checkLikeResponse

    /*
        좋아요 수 변수
     */
        private val _likeCount = MutableStateFlow<Int>(0)
        val likeCount: StateFlow<Int> = _likeCount

    /*
    차단 등록
     */
        private val _saveBlockResponse = MutableStateFlow(SaveBlockResponse())
        val saveBlockResponse: StateFlow<SaveBlockResponse> = _saveBlockResponse

        /*
        본인의 게시글인기?
         */
        private val _isMyPost = MutableStateFlow(false)
        val isMyPost: StateFlow<Boolean> = _isMyPost

        /*
        현재 로그인한 유저 정보
         */
        private var userInfo = UserInfo()

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
                DetailMovie(null, null, null, null, null, null, null, null, null, null),
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
                    nickname = "",
                ),
            )
        val getReportResponse: StateFlow<GetReportResponse> = _getReportResponse.asStateFlow()

        // 좋아요 등록
        private fun saveLike() {
            viewModelScope.launch {
                registerLikeUseCase(
                    SaveLikeRequest(
                        targetId = reportId,
                        type = "report",
                    ),
                ).collect {
                    if (it != null) {
                        updateLikeCount()
                    }
                }
            }
        }

    /*
좋아요 취소
     */
        private fun cancelLike() {
            viewModelScope.launch {
                val likeId = _checkLikeResponse.value.likeId
                cancelLikeUseCase(likeId!!).collect {
                    if (it != null) {
                        updateLikeCount()
                    }
                }
            }
        }

        // 좋아요 토글
        private fun toggleLike() {
            viewModelScope.launch {
                checkLikeStateUseCase(reportId, "report").collect {
                    if (it != null) {
                        _checkLikeResponse.value = it
                        if (it.isLike) {
                            cancelLike()
                        } else {
                            saveLike()
                        }
                        val updatedLikeState = _checkLikeResponse.value.copy(isLike = !it.isLike)
                        _checkLikeResponse.value = updatedLikeState
                    }
                }
            }
        }

        // 좋아요 수 업데이트
        private fun updateLikeCount() {
            viewModelScope.launch {
                countLikeUseCase(reportId).collect {
                    if (it != null) {
                        _likeCount.value = it.countLike
                    }
                }
            }
        }

    /*
    계정 차단
     */
        private fun saveBlock() {
            viewModelScope.launch {
                if (_getReportResponse.value.userId == null) return@launch
                saveBlockUseCase(SaveBlockRequest(targetId = _getReportResponse.value.userId!!)).collect {
                    if (it == null) return@collect
                    _saveBlockResponse.value = it
                    sendEffect(BodyMovieReportEffect.BlockSuccess)
                }
            }
        }

    /*
    감상문 신고
     */
        private fun saveComplaint() {
            viewModelScope.launch {
                saveComplaintUseCase(
                    SaveComplaintRequest(
                        targetId = _getReportResponse.value.reportId,
                        type = "report",
                    ),
                ).collect {
                    if (it != null) {
                        sendEffect(BodyMovieReportEffect.ComplaintSuccess)
                    }
                }
            }
        }

    /*
    사용자 닉네임
     */
        private fun getUserNickName(userId: String) {
            viewModelScope.launch {
                getUserInfoUseCase(userId).collect {
                    if (it != null) {
                        userNickName = it.nickname
                    }
                }
            }
        }

    /*
      팔로우 토글
     */
        private fun toggleFollow() {
            if (_checkIsFollowResponse.value.isFollowing) {
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
        private val _checkIsFollowResponse = MutableStateFlow(CheckIsFollowResponse())
        val checkIsFollowResponse: StateFlow<CheckIsFollowResponse> = _checkIsFollowResponse

    /*
 팔로우 등록
     */
        private fun saveFollow() {
            viewModelScope.launch {
                _getReportResponse.value.userId?.let {
                    saveFollowUseCase(it).collect {
                        if (it != null) {
                            val updatedFollowInfo = _checkIsFollowResponse.value.copy(isFollowing = true, followId = it.followId)
                            _checkIsFollowResponse.value = updatedFollowInfo
                            sendEffect(BodyMovieReportEffect.SaveFollow)
                        }
                    }
                }
            }
        }

    /*
    팔로우 취소
     */
        private fun cancelFollow() {
            viewModelScope.launch {
                // 팔로우 여부 검사할 때 얻은 FollowId를 이용해서 팔로우 취소
                cancelFollowUseCase(_checkIsFollowResponse.value.followId).collect {
                    if (it != null && it) {
                        val updatedFollowInfo = _checkIsFollowResponse.value.copy(isFollowing = false)
                        _checkIsFollowResponse.value = updatedFollowInfo
                        sendEffect(BodyMovieReportEffect.CancelFollow)
                    }
                }
            }
        }

    /*
    팔로우 여부 검사
     */
        private fun checkIsFollow() {
            viewModelScope.launch {
                if (_getReportResponse.value.userId == null) return@launch
                checkIsFollowUseCase(_getReportResponse.value.userId!!).collect {
                    if (it != null) {
                        _checkIsFollowResponse.value = it
                    }
                }
            }
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
                        sendEffect(BodyMovieReportEffect.ShowMovieInfo)
                    }
                }
            }
        }

        private fun deleteReport() {
            viewModelScope.launch {
                deleteReportUseCase(reportId).collect {
                    Timber.d("삭제 결과 :$it")
                }
            }
        }

        private fun getReport() {
            viewModelScope.launch {
                getReportUseCase(reportId).collect {
                    if (it != null) {
                        _getReportResponse.value = it
                        _likeCount.value = it.likeCount
                        checkLikeState()
                        checkIsFollow()
                        searchMovieDetail(it.movieId)
                        if (userInfo.userId == _getReportResponse.value.userId) {
                            _isMyPost.value = true
                        } else {
                            // else 문을 적어주지 않으면 안되는 것이었다. 코드를 잘못 작성해줬음.
                            _isMyPost.value = false
                        }
                        _getReportResponse.value.userId?.let { it1 -> getUserNickName(userId = it1) }
                        sendEffect(BodyMovieReportEffect.ShowReport)
                    }
                }
            }
        }

        private fun checkLikeState() {
            viewModelScope.launch {
                checkLikeStateUseCase(reportId, "report").collect {
                    if (it != null) {
                        _checkLikeResponse.value = it
                    }
                }
            }
        }

        override fun handleEvent(event: BodyMovieReportEvent) {
            when (event) {
                is BodyMovieReportEvent.ClickLikeButton -> {
                    toggleLike()
                }
                is BodyMovieReportEvent.SaveBlock -> {
                    saveBlock()
                }
                is BodyMovieReportEvent.SaveComplaint -> {
                    saveComplaint()
                }
                is BodyMovieReportEvent.ClickFollow -> {
                    toggleFollow()
                }
                is BodyMovieReportEvent.ClickMoreButton -> {
                    getMovieContent()
                }
                is BodyMovieReportEvent.ShowReport -> {
                    getReport()
                }
                is BodyMovieReportEvent.ShowMovieInfo -> {
                    searchMovieDetail(event.movieId)
                }
                is BodyMovieReportEvent.UpdateReport -> {
                }
                is BodyMovieReportEvent.DeleteReport -> {
                    deleteReport()
                }
                else -> {}
            }
        }
    }
