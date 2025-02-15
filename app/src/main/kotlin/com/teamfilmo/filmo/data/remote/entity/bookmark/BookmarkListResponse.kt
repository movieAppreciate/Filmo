package com.teamfilmo.filmo.data.remote.entity.bookmark

import kotlinx.serialization.Serializable

@Serializable
data class BookmarkListResponse(
    val bookmarkList: List<BookmarkResponse>,
    val hasNext: Boolean,
)
