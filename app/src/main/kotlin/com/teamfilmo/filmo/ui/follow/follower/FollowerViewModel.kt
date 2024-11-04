package com.teamfilmo.filmo.ui.follow.follower

import androidx.lifecycle.viewModelScope
import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import com.teamfilmo.filmo.data.remote.model.follow.MutualFollowUserInfo
import com.teamfilmo.filmo.domain.follow.CheckIsFollowUseCase
import com.teamfilmo.filmo.domain.follow.GetFollowerListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel
class FollowerViewModel
    @Inject
    constructor(
        private val checkIsFollowUseCase: CheckIsFollowUseCase,
        private val getFollowerListUseCase: GetFollowerListUseCase,
    ) : BaseViewModel<FollowerEffect, FollowerEvent>() {
        private val _mutualFollowerList = MutableStateFlow<List<MutualFollowUserInfo>>(emptyList())
        val mutualFollowerList: StateFlow<List<MutualFollowUserInfo>> = _mutualFollowerList

    /*
팔로우 리스트
 구현 : zip 연산자 사용
     */
        fun getFollowerList(userId: String) {
            viewModelScope.launch {
                getFollowerListUseCase(userId)
                    .flatMapLatest { followerResponse ->
                        // followingResponse가 null이 아니고 followingUserInfoList가 있는 경우에만 처리
                        val userList = followerResponse.followerUserInfoList ?: emptyList()
                        // 각 유저의 팔로우 상태를 확인하는 Flow 리스트 생성
                        val followStatusFlows =
                            userList.map { user ->
                                checkIsFollowUseCase(user.userId ?: "").map { followResponse ->
                                    MutualFollowUserInfo(
                                        email = user.email,
                                        userId = user.userId,
                                        type = user.type,
                                        nickname = user.nickname,
                                        profileUrl = user.profileUrl,
                                        lastLoginDate = user.lastLoginDate,
                                        introduction = user.introduction,
                                        roles = user.roles,
                                        createDate = user.createDate,
                                        lastModifiedDate = user.lastModifiedDate,
                                        isFollowing = followResponse?.isFollowing,
                                    )
                                }
                            }

                        // Flow 리스트를 단일 Flow<List>로 결합
                        if (followStatusFlows.isEmpty()) {
                            flow { emit(emptyList()) }
                        } else {
                            combine(followStatusFlows) { it.toList() }
                        }
                    }.catch { throwable ->
                        // 에러 처리
                        // _error.value = throwable.message
                    }.collect { mutualFollowList ->
                        _mutualFollowerList.value = mutualFollowList
                    }
            }
        }
    }
