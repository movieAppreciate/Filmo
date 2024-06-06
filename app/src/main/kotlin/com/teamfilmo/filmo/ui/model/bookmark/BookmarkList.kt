package com.teamfilmo.filmo.ui.model.bookmark

import kotlinx.serialization.Serializable

@Serializable
data class BookmarkList(
    val hasNext: Boolean,
    val bookmarkList: List<BookmarkResponse>,
)
