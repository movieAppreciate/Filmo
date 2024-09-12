package com.teamfilmo.filmo.data.remote.model.bookmark

import kotlinx.serialization.Serializable

@Serializable
data class SaveBookmarkRequest(
    val reportId: String,
)
