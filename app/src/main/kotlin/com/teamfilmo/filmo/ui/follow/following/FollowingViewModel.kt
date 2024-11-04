package com.teamfilmo.filmo.ui.follow.following

import androidx.lifecycle.viewModelScope
import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import com.teamfilmo.filmo.data.remote.model.follow.MutualFollowUserInfo
import com.teamfilmo.filmo.domain.follow.CheckIsFollowUseCase
import com.teamfilmo.filmo.domain.follow.GetFollowingListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel
class FollowingViewModel
    @Inject
    constructor(
        private val checkIsFollowUseCase: CheckIsFollowUseCase,
        private val getFollowingListUseCase: GetFollowingListUseCase,
    ) : BaseViewModel<FollowingEffect, FollowingEvent>() {
        private val _followingList = MutableStateFlow<List<MutualFollowUserInfo>>(emptyList())
        val followingList: StateFlow<List<MutualFollowUserInfo>> = _followingList

        fun getFollowingList(userId: String) {
            viewModelScope.launch {
                getFollowingListUseCase(userId)
                    .flatMapLatest { followingResponse ->
                        // followingResponse가 null이 아니고 followingUserInfoList가 있는 경우에만 처리
                        val userList = followingResponse?.followingUserInfoList ?: emptyList()
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
                        _followingList.value = mutualFollowList
                    }
            }
        }
    }

//        // 팔로잉 리스트
//        fun getFollowingList(userId: String) {
//            viewModelScope.launch {
//                getFollowingListUseCase(userId).collect {
//                    if (it != null) {
//                        _followingList.value = it.followingUserInfoList
//                    }
//                }
//            }
//        }
