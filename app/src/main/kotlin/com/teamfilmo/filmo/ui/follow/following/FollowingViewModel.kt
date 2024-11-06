package com.teamfilmo.filmo.ui.follow.following

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.teamfilmo.filmo.base.viewmodel.BaseViewModel
import com.teamfilmo.filmo.data.remote.model.follow.MutualFollowUserInfo
import com.teamfilmo.filmo.domain.follow.CheckIsFollowUseCase
import com.teamfilmo.filmo.domain.follow.GetFollowingListUseCase
import com.teamfilmo.filmo.ui.follow.paging.FollowingPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
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

    /*
    팔로잉 페이징 데이터
     */
        private val _pagingFollowingData = MutableStateFlow<PagingData<MutualFollowUserInfo>>(PagingData.empty())
        val pagingFollowingData = _pagingFollowingData.asStateFlow()

        fun getFollowingList(userId: String) {
            viewModelScope.launch {
                Pager(
                    config =
                        PagingConfig(
                            pageSize = 20,
                            enablePlaceholders = false,
                            prefetchDistance = 3,
                        ),
                    pagingSourceFactory = {
                        FollowingPagingSource(
                            checkIsFollowUseCase,
                            getFollowingListUseCase,
                            userId,
                        )
                    },
                ).flow.cachedIn(viewModelScope).collect {
                    _pagingFollowingData.value = it
                }
            }
        }
    }
