package com.teamfilmo.filmo.data.remote.entity.bookmark

import kotlinx.serialization.Serializable

@Serializable
data class SaveBookmarkRequest(
    val reportId: String,
)
