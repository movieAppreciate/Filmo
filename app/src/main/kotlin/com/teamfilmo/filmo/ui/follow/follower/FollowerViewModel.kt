package com.teamfilmo.filmo.ui.follow.follower

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import com.teamfilmo.filmo.data.remote.entity.follow.MutualFollowUserInfo
import com.teamfilmo.filmo.domain.follow.CheckIsFollowUseCase
import com.teamfilmo.filmo.domain.follow.GetFollowerListUseCase
import com.teamfilmo.filmo.ui.follow.paging.FollowerPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class FollowerViewModel
    @Inject
    constructor(
        private val checkIsFollowUseCase: CheckIsFollowUseCase,
        private val getFollowerListUseCase: GetFollowerListUseCase,
    ) : BaseViewModel<FollowerEffect, FollowerEvent>() {
        // 팔로워 리스트 페이징 데이터
        private val _pagingFollowerData = MutableStateFlow<PagingData<MutualFollowUserInfo>>(PagingData.empty())
        val pagingFollowerData: StateFlow<PagingData<MutualFollowUserInfo>> = _pagingFollowerData

        fun getFollowerList(userId: String) {
            viewModelScope.launch {
                viewModelScope.launch {
                    Pager(
                        config =
                            PagingConfig(
                                pageSize = 20,
                                enablePlaceholders = false,
                                prefetchDistance = 3,
                            ),
                        pagingSourceFactory = {
                            FollowerPagingSource(
                                checkIsFollowUseCase = checkIsFollowUseCase,
                                getFollowerListUseCase = getFollowerListUseCase,
                                userId = userId,
                            )
                        },
                    ).flow.cachedIn(viewModelScope).collect {
                        _pagingFollowerData.value = it
                    }
                }
            }
        }
    }
