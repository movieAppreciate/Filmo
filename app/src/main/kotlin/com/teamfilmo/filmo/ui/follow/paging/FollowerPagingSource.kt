package com.teamfilmo.filmo.ui.follow.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.teamfilmo.filmo.data.remote.model.follow.MutualFollowUserInfo
import com.teamfilmo.filmo.domain.follow.CheckIsFollowUseCase
import com.teamfilmo.filmo.domain.follow.GetFollowerListUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class FollowerPagingSource(
    private val checkIsFollowUseCase: CheckIsFollowUseCase,
    private val getFollowerListUseCase: GetFollowerListUseCase,
    private val userId: String,
) : PagingSource<String, MutualFollowUserInfo>() {
    override fun getRefreshKey(state: PagingState<String, MutualFollowUserInfo>): String? =
        state.anchorPosition?.let {
            state.closestItemToPosition(it)?.userId
        }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, MutualFollowUserInfo> =
        try {
            val lastReportId = if (params is LoadParams.Refresh) null else params.key
            val followerData =
                getFollowerListUseCase(userId, lastReportId)
                    .map { followerResponse ->
                        val hasNext = followerResponse!!.hasNext
                        val userList = followerResponse.followerUserInfoList ?: emptyList()
                        userList.map { user ->
                            val followResponse = checkIsFollowUseCase(user.userId ?: "").first()
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
                        } to hasNext
                    }.first()

            val (items, hasNext) = followerData
            val nextKey =
                if (!hasNext) null else items.lastOrNull()?.userId

            LoadResult.Page(
                data = items,
                prevKey = null,
                nextKey = nextKey,
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
}
