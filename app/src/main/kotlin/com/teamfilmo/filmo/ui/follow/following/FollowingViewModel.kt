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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class FollowingViewModel
    @Inject
    constructor(
        private val checkIsFollowUseCase: CheckIsFollowUseCase,
        private val getFollowingListUseCase: GetFollowingListUseCase,
    ) : BaseViewModel<FollowingEffect, FollowingEvent>() {
        private val _followingList = MutableStateFlow<List<MutualFollowUserInfo>>(emptyList())
        val followingList: StateFlow<List<MutualFollowUserInfo>> = _followingList

        // 1. map 으로 순차적으로 처리하기
        fun getFollowingList(userId: String) {
            viewModelScope.launch {
                getFollowingListUseCase(userId)
                    .map { followingResponse ->
                        println("1. 팔로잉 응답 처리 시작")

                        val userList = followingResponse?.followingUserInfoList ?: emptyList()

                        // 순차적으로 각 사용자의 팔로우 상태 확인
                        userList.map { user ->
                            println("2. 사용자 ${user.userId} 팔로우 상태 확인 시작")
                            val followResponse = checkIsFollowUseCase(user.userId ?: "").first()
                            println("3. 사용자 ${user.userId} 팔로우 상태 확인 완료")
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
                    }.catch {
                        Timber.e("failed to get following list with followingState")
                    }.collect {
                        _followingList.value = it
                    }
            }
        }

        // 2 . 병렬 처리(map & combine 사용)
        fun getFollowingListParallel(userId: String) {
            viewModelScope.launch {
                getFollowingListUseCase(userId)
                    .map { followingResponse ->
                        println("1. 팔로잉 응답 처리 시작")
                        // null 체크 후 리스트 가져오기
                        val userList = followingResponse?.followingUserInfoList ?: emptyList()

                        // 각 사용자별로 Flow 생성
                        val followStatusFlows =
                            userList.map { user ->
                                println("2. 사용자 ${user.userId} 팔로우 상태 확인 시작")
                                // 각 사용자의 팔로우 상태를 확인하는 Flow 생성
                                checkIsFollowUseCase(user.userId ?: "").map { followResponse ->
                                    println("3. 사용자 ${user.userId} 팔로우 상태 확인 완료")
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

                        // Flow 리스트가 비어있는 경우 처리
                        if (followStatusFlows.isEmpty()) {
                            flow { emit(emptyList()) }
                        } else {
                            // combine을 사용해 모든 Flow를 병렬로 처리
                            println("4. 팔로잉 리스트 병렬 처리 시작")
                            combine(followStatusFlows) { userInfoArray ->
                                println("5. 팔로잉 리스트 병렬 처리 완료")
                                userInfoArray.toList()
                            }
                        }
                    }.collect {
                        _followingList.value = it.first()
                    }
            }
        }
    }
