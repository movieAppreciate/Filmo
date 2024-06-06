package com.teamfilmo.filmo.ui.model.bookmark

import kotlinx.serialization.Serializable

@Serializable
data class BookmarkResponse(
    val bookmarkId: Int,
    val userId: String,
    val reportId: String,
)
