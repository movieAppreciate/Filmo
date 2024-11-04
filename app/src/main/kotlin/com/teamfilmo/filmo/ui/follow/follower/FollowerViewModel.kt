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
import kotlinx.coroutines.flow.first
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

        fun getFollowerList(userId: String) {
            viewModelScope.launch {
                getFollowerListUseCase(userId)
                    .map {
                        it.followerUserInfoList
                            .map { userInfo ->
                                userInfo.userId?.let { it1 ->
                                    val isFollowing = checkIsFollowUseCase(it1).first()?.isFollowing ?: false
                                    MutualFollowUserInfo(
                                        email = userInfo.email,
                                        userId = userInfo.userId,
                                        type = userInfo.type,
                                        nickname = userInfo.nickname,
                                        profileUrl = userInfo.profileUrl,
                                        lastLoginDate = userInfo.lastLoginDate,
                                        introduction = userInfo.introduction,
                                        roles = userInfo.roles,
                                        createDate = userInfo.createDate,
                                        lastModifiedDate = userInfo.lastModifiedDate,
                                        isFollowing = isFollowing,
                                    )
                                } ?: MutualFollowUserInfo()
                            }
                    }.collect {
                        _mutualFollowerList.value = it
                    }
            }
        }
    }
