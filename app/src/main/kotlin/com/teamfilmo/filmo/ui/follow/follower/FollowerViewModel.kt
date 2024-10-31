package com.teamfilmo.filmo.ui.follow.follower

import androidx.lifecycle.viewModelScope
import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import com.teamfilmo.filmo.data.remote.model.follow.FollowUserInfo
import com.teamfilmo.filmo.domain.follow.GetFollowerListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class FollowerViewModel
    @Inject
    constructor(
        private val getFollowerListUseCase: GetFollowerListUseCase,
    ) : BaseViewModel<FollowerEffect, FollowerEvent>() {
//        init {
//            getFollowerList()
//        }

        // 팔로워 리스트
        private val _followerList = MutableStateFlow<List<FollowUserInfo>>(emptyList())
        val followerList: StateFlow<List<FollowUserInfo>> = _followerList

    /*
팔로우 리스트
     */
        fun getFollowerList(userId: String) {
            viewModelScope.launch {
                getFollowerListUseCase(userId).collect {
                    _followerList.value = it.followerUserInfoList
                    Timber.d("팔로워리스트 : $it.followerUserInfoList")
                }
            }
        }
    }
