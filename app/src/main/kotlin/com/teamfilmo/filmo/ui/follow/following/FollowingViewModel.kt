package com.teamfilmo.filmo.ui.follow.following

import androidx.lifecycle.viewModelScope
import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import com.teamfilmo.filmo.data.remote.model.follow.FollowUserInfo
import com.teamfilmo.filmo.domain.follow.GetFollowingListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class FollowingViewModel
    @Inject
    constructor(
        private val getFollowingListUseCase: GetFollowingListUseCase,
    ) : BaseViewModel<FollowingEffect, FollowingEvent>() {
        private val _followingList = MutableStateFlow<List<FollowUserInfo>>(emptyList())
        val followingList: StateFlow<List<FollowUserInfo>> = _followingList

        // 팔로잉 리스트
        fun getFollowingList(userId: String) {
            viewModelScope.launch {
                getFollowingListUseCase(userId).collect {
                    if (it != null) {
                        _followingList.value = it.followingUserInfoList
                    }
                }
            }
        }
    }
