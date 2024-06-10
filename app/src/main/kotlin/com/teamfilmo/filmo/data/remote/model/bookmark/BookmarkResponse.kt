package com.teamfilmo.filmo.data.remote.model.bookmark

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookmarkResponse(
    @SerialName("bookmarkId")
    val id: Long,
    val userId: String,
    val reportId: String,
)
